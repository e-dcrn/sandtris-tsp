package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


import javafx.scene.paint.Color;

/**
 * Représente la zone de jeu principale où les blocs tombent, s'empilent et interagissent.
 * Cette classe gère l'ensemble des blocs présents dans une grille 2D. Elle fournit
 * des fonctions pour :
 * - Créer des blocs
 * - Détecter les collisions
 * - Animer les blocs (gravité, mouvement aléatoire)
 * - Rechercher et supprimer les clusters de blocs connectés.
 * @param width la largeur de la grille (en nombre de cellules)
 * @param height la hauteur de la grille (en nombre de cellules)
 * @param squareRatio la taille (en particules) d’un carré standard
 * @param blocks Grille principale de la zone de jeu, contenant tous les blocs placés
 * @param newBlocks Liste des nouveaux blocs ajoutés lors du dernier tour de jeu.
 **/
public class SandArea {
    private final int width;
    private final int height;
    private final int squareRatio;
    private Block[][] blocks;
    private final ArrayList<Block> newBlocks;
    private final ArrayList<Block> deletedBlocks;
    private final ArrayList<Block> movedBlocks;
    private IntegerProperty score = new SimpleIntegerProperty(0);


    /**
     * Constructeur de la zone de jeu.
     * @param width Largeur de la scène en unités "particule".
     * @param height Hauteur de la scène en unités "particule".
     * @param squareRatio Taille d'un carré en unités "particule".
     */
    public SandArea(int width, int height, int squareRatio) {
        this.width = width;
        this.height = height;
        this.squareRatio = squareRatio;
        this.blocks = new Block[height][width];
        newBlocks = new ArrayList<>();
        deletedBlocks = new ArrayList<>();
        movedBlocks = new ArrayList<>();
    }

    public ArrayList<Block> getNewBlocks(){
        return newBlocks;
    }

    public ArrayList<Block> getDeletedBlocks(){
        return deletedBlocks;
    }

    public ArrayList<Block> getMovedBlocks(){
        return movedBlocks;
    }

    /**
     * Teste les collisions pour un carré de tétromino à une position donnée.
     * Les tétrominos sont composés de 4 carrés.
     * @param newX Coordonnée X du carré en unités "particule".
     * @param newY Coordonnée Y du carré en unités "particule".
     * @return Vrai s'il y a collision, sinon faux.
     */
    public boolean checkCollision(int newX, int newY) {
        if (newY >= height - squareRatio) return true;
        int gridStartX = newX;
        int gridEndX = newX + squareRatio - 1;
        int gridStartY = newY;
        int gridEndY = newY + squareRatio - 1;

        for (int i = gridStartY; i <= gridEndY; i++) {
            for (int j = gridStartX; j <= gridEndX; j++) {
                if (i >= 0 && i < height && j >= 0 && j < width) {
                    if (blocks[i][j] != null) return true;
                }
            }
        }
        return false;
    }

    /**
     * Crée un bloc de particules à la position donnée.
     * @param x Coordonnée X du carré en unités "particule".
     * @param y Coordonnée Y du carré en unités "particule".
     */
    public void createBlock(int x, int y, Color current_couleur) {
        for (int i = 0; i < squareRatio; i++) {
            for (int j = 0; j < squareRatio; j++) {
                Block block = new Block(x + j, y + i, current_couleur);
                blocks[y + i][x + j] = block;
                newBlocks.add(block);
            }
        }
    }

    public List<List<Block>> findCluster() {
        Set<Block> visited = new HashSet<>();
        List<List<Block>> allClusters = new ArrayList<>();
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
    
                Block current_block = blocks[y][x];
                if (current_block == null || visited.contains(current_block)) continue;
    
                Color color = current_block.getColor();
                List<Block> cluster = new ArrayList<>();
    
                Queue<Block> queue = new LinkedList<>();
                queue.add(current_block);
                visited.add(current_block);
    
                while (!queue.isEmpty()) {
                    Block current = queue.poll();
                    cluster.add(current);
    
                    int cx = current.getX();
                    int cy = current.getY();
    
                    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                    for (int[] dir : directions) {
                        int nx = cx + dir[0];
                        int ny = cy + dir[1];
    
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Block neighbor = blocks[ny][nx];
                            if (neighbor != null && !visited.contains(neighbor) && neighbor.getColor().equals(color)) {
                                queue.add(neighbor);
                                visited.add(neighbor);
                            }
                        }
                    }
                }
                allClusters.add(cluster);
            }
        }

        return allClusters;
    }

    private List<List<Block>> findClusterToDelete() {
        List<List<Block>> allClusters = findCluster();
        List<List<Block>> clusterToDelete = new ArrayList<>();
        for (int i = 0; i < allClusters.size(); i ++) {
            boolean touch_left = false;
            boolean touch_right = false;
            for (int j = 0; j < allClusters.get(i).size(); j ++) {
                int x_position = allClusters.get(i).get(j).getX();
                if (x_position <= 0) {
                    touch_left = true;
                }
                if (x_position >= width-1) {
                    touch_right = true;
                }
            }
            if (touch_left && touch_right) {
                clusterToDelete.add(allClusters.get(i));
            }
        }

        for (int i = 0; i < clusterToDelete.size(); i ++) {
            System.out.println(clusterToDelete.get(i).size());
        }

        return clusterToDelete;

    }
    
    public IntegerProperty scoreProperty() {
        return score;
    }
    
    /**
     * Ajoute des points au score.
     * @param points Le nombre de points à ajouter.
     */
    public void addScore(int points) {
        score.set(score.get() + points);
    }
    
    /**
     * Supprime les blocs appartenant aux clusters qui doivent être supprimés.
     */
    public void removeBlocksToDelete() {
        List<List<Block>> clusterToDelete = findClusterToDelete();

        
        for (int i = 0; i < clusterToDelete.size(); i ++) {
        	int clusterSize = clusterToDelete.get(i).size();
            addScore(clusterSize);
            for (int j = 0; j < clusterToDelete.get(i).size(); j ++) {
                deletedBlocks.add(clusterToDelete.get(i).get(j));
                score.set(score.get() + 1);
            }
        }
        
        for (List<Block> cluster : clusterToDelete) {
            for (Block b : cluster) {
                blocks[b.getY()][b.getX()] = null;
            }
        }
    }
    

    /**
     * Anime les blocs pour la frame actuelle, en appliquant la physique de chute et de glissement.
     */
    public void animateBlocks() {
        Random random = new Random();
        for (int i = height - 1; i >= 0; i--) {
            // Le parcours de cette boucle se fait de manière aléatoire, soit de gauche à droite, soit de droite à gauche.
            double moveLeftOrRight = random.nextDouble();
            int start = moveLeftOrRight < 0.5 ? 0 : width - 1;
            int end = moveLeftOrRight < 0.5 ? width : -1;
            int step = moveLeftOrRight < 0.5 ? 1 : -1;
            for (int j = start; j != end; j += step) {
                if (blocks[i][j] != null) { // Test de la présence d'une particule
                    // Détermine aléatoirement la direction de chute prioritaire (gauche ou droite).
                    moveLeftOrRight = random.nextDouble();
                    int balanceLeftAndRight = -1;
                    boolean borderSideCondition1 = j < width - 1;
                    boolean borderSideCondition2 = j > 0;
                    if(moveLeftOrRight<0.5){ 
                        balanceLeftAndRight = 1;
                        borderSideCondition1 = j > 0;
                        borderSideCondition2 = j < width - 1;
                    }

                    // Gère la chute libre d'une particule.
                    // La particule tombe, avec une probabilité de se déplacer latéralement.
                    if (i < height - 1 && blocks[i + 1][j] == null) {
                        double sideMoveProba = random.nextDouble();
                        double seuil = 0.2;
                        if(sideMoveProba<seuil){
                            if(borderSideCondition1 && blocks[i][j - 1*balanceLeftAndRight] == null){
                                moveBlock(i, j, i, j - 1*balanceLeftAndRight);
                            }
                            else if(borderSideCondition2 && blocks[i][j + 1*balanceLeftAndRight] == null){
                                moveBlock(i, j, i, j + 1*balanceLeftAndRight);
                            }
                        }
                        else{
                            moveBlock(i, j, i + 1, j);
                        }
                    }

                    // Gère l'écoulement des particules sur les côtés lorsqu'elles sont empilées.
                    else if (borderSideCondition1 && i < height - 1 && blocks[i + 1][j - 1*balanceLeftAndRight] == null && blocks[i][j - 1*balanceLeftAndRight] == null) {
                        moveBlock(i, j, i, j - 1*balanceLeftAndRight);
                    }
                    else if (borderSideCondition2 && i < height - 1 && blocks[i + 1][j + 1*balanceLeftAndRight] == null && blocks[i][j + 1*balanceLeftAndRight] == null) {
                        moveBlock(i, j, i, j + 1*balanceLeftAndRight);
                    }
                }
            }
        }
    }

    /**
     * Déplace un bloc d'une position à une autre dans la grille.
     * Met également à jour les coordonnées du bloc.
     * @param oldI Ancienne coordonnée Y du bloc.
     * @param oldJ Ancienne coordonnée X du bloc.
     * @param newI Nouvelle coordonnée Y du bloc.
     * @param newJ Nouvelle coordonnée X du bloc.
     */
    private void moveBlock(int oldI, int oldJ, int newI, int newJ) {
        Block block = blocks[oldI][oldJ];
        blocks[oldI][oldJ] = null;
        blocks[newI][newJ] = block;
        block.setXY(newJ,newI);
        movedBlocks.add(block);
    }

    public void setBlock(Block block) {
        blocks[block.getY()][block.getX()] = block;
    }
    
    public void resetscore() {
        score.set(0);
    }

}