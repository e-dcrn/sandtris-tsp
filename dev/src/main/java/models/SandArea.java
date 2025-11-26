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
 * des fonctions de :
 * Création de blocs
 * Détection de collision
 * Animation des blocs (gravité, mouvement aléatoire)
 * Recherche et suppression de clusters de blocs connectés
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
     * Il s'agit du constructeur.
     * @param width Il s'agit de la largeur de la scene en unité "particule"
     * @param height Il s'agit de la hauteur de la scene en unité "particule"
     * @param squareRatio Il s'agit de la taille du carré qui tombe en continue en unité de particule
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
     * Cet fonction teste les collisions pour un seul carré de tétromino. 
     * Sachant que les tétrominos sont un arangement de 4 carré.
     * @param newX Il s'agit de la coordonné X du carré en unité "particule"
     * @param newY Il s'agit de la coordonné Y du carré en unité "particule"
     * @return Retourn True s'il y à collision et False sinon
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
     * Cette fonction transforme le carré en particule.
     * @param x Il s'agit de la coordonné X du carré en unité "particule"
     * @param y Il s'agit de la coordonné Y du carré en unité "particule"
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
    
 // Méthode pour ajouter des points au score
    public void addScore(int points) {
        score.set(score.get() + points);
    }
    
    /**
     * Supprime les blocs appartenant à des clusters à supprimer
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
     * Cette fonction fait évoluer l'ensemble des particules d'une frame pour qu'elle respecte la physique choisit.
     */
    public void animateBlocks() {
        Random random = new Random();
        for (int i = height - 1; i >= 0; i--) {
            //Le parcours de la cette boucle se fait soit dans le sens des j croissants soit dans le sens des j décroissant.
            //C'est à ça que sert cette section.
            //début de section
            double moveLeftOrRight = random.nextDouble();
            int start = moveLeftOrRight < 0.5 ? 0 : width - 1;
            int end = moveLeftOrRight < 0.5 ? width : -1;
            int step = moveLeftOrRight < 0.5 ? 1 : -1;
            for (int j = start; j != end; j += step) {
            //fin de section
                if (blocks[i][j] != null) { // Test de la présence d'une particule
                    //La section suivante sert à choisir si on tombe en priorité à droite ou à gauche lorsqu'on à le choix
                    //début de section
                    moveLeftOrRight = random.nextDouble();
                    int balanceLeftAndRight = -1;
                    boolean borderSideCondition1 = j < width - 1;
                    boolean borderSideCondition2 = j > 0;
                    if(moveLeftOrRight<0.5){ 
                        balanceLeftAndRight = 1;
                        borderSideCondition1 = j > 0;
                        borderSideCondition2 = j < width - 1;
                    }
                    //fin de section

                    //Cette section traite le cas où une particule est en chute libre.
                    //Si la particule peut tomber alors elle tombe.
                    //Mais elle à une probabilité de ce déplacer sur le coté (comme s'il y avait du vent).
                    //début de section
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
                    //fin de section

                    //Cette section gère les empillements de particule.
                    //Si un particule à un écart de plus de 1 unité de haut à gauche ou à droite alors elle tombe du coté en question
                    //début de section
                    else if (borderSideCondition1 && i < height - 1 && blocks[i + 1][j - 1*balanceLeftAndRight] == null && blocks[i][j - 1*balanceLeftAndRight] == null) {
                        moveBlock(i, j, i, j - 1*balanceLeftAndRight);
                    }
                    else if (borderSideCondition2 && i < height - 1 && blocks[i + 1][j + 1*balanceLeftAndRight] == null && blocks[i][j + 1*balanceLeftAndRight] == null) {
                        moveBlock(i, j, i, j + 1*balanceLeftAndRight);
                    }
                    //fin de section
                }
            }
        }
    }

    /**
     * Cette fonction déplace une particule dans la matrice de la postion (oldJ,oldI) à (newJ,newI).
     * Elle met à jours ses coordonnées interne pour pouvoir être affiché par le moteur graphique.
     * @param oldI Coordonné Y de la particule en unité de "particule"
     * @param oldJ Coordonné X de la particule en unité de "particule"
     * @param newI Coordonné Y d'arrivé de la particule en unité de "particule"
     * @param newJ Coordonné X d'arrivé de la particule en unité de "particule"
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