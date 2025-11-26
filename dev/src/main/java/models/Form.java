package models;

import java.util.Random;

/**
 * Représente une forme Tetris (tétrimino) composée de blocs disposés selon une matrice.
 * @param matrice Matrice représentant la forme du tétrimino
 * @param rightXSpace Nombre de colonnes non vides à droite de la forme.
 * @param bottomYSpace Nombre de lignes non vides en bas de la forme.
 **/
public class Form {
    private int[][] matrice;
    private int rightXSpace;
    private int bottomYSpace;
    private int leftXSpace;
    private int topXSpace;
    private int width;
    private int height;
    
    private static final int[][][] FORMES_MATRICES = {
        // I
        {{1,1,1,1}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}},
        // J
        {{1,0,0}, {1,1,1}, {0,0,0}},
        // L
        {{0,0,1}, {1,1,1}, {0,0,0}},
        // O
        {{1,1}, {1,1}},
        // S
        {{0,1,1}, {1,1,0}, {0,0,0}},
        // T
        {{0,1,0}, {1,1,1}, {0,0,0}},
        // Z
        {{1,1,0}, {0,1,1}, {0,0,0}}
    };
    
    private Form(int[][] matrice) {
        this.matrice = matrice;
        rightXSpace = calculateRightXSpace();
        leftXSpace = calculateLeftXSpace();
        topXSpace = calculateTopYSpace();
        bottomYSpace = calculateBottomYSpace();
        calculateDim();
    }

    /**
     * Crée une forme parmis I,J,L,O,S,T,Z
     * @param type entier compris entre 0 et 6 inclus
     * @throws IllegalArgumentException type n'est pas une forme valide
     * @return un type de forme
     */
    public static Form createForm(int type) {
        if (type < 0 || type >= FORMES_MATRICES.length) {
            throw new IllegalArgumentException("Type de forme invalide: " + type);
        }
        Form form = new Form(FORMES_MATRICES[type]);
        return form;
    }

    /**
     * @return une forme aléatoire
     */
    public static Form getFormeAleatoire(int blockSize) {
        int type = new Random().nextInt(FORMES_MATRICES.length);
        return createForm(type);
    }

    /**
     *
     * @return la position de la colonne la plus à droite contenant un bloc
     */
    private int calculateRightXSpace() {
        int maxCol = 0;
        // Parcours toutes les lignes
        for (int[] ligne : matrice) {
            // Trouve la dernière colonne non vide dans cette ligne
            for (int col = ligne.length - 1; col >= 0; col--) {
                if (ligne[col] == 1) {
                    if (col + 1 > maxCol) {
                        maxCol = col + 1;
                    }
                    break;
                }
            }
        }
        return maxCol;


    }

    private int calculateLeftXSpace() {
        int emptyCols = 0;
        boolean foundBlock;
    
        // Parcourt les colonnes de gauche à droite
        for (int col = 0; col < matrice[0].length; col++) {
            foundBlock = false;
            
            // Vérifie si la colonne contient au moins un bloc
            for (int row = 0; row < matrice.length; row++) {
                if (matrice[row][col] == 1) {
                    foundBlock = true;
                    break;
                }
            }
            
            if (!foundBlock) {
                emptyCols++;
            } else {
                break; // On a trouvé la première colonne non vide
            }
        }
        
        return emptyCols;
    }
    
    private int calculateBottomYSpace() {
        int maxRow = 0;
        // Parcours toutes les lignes de haut en bas
        for (int row = matrice.length - 1; row >= 0; row--) {
            // Vérifie si la ligne contient au moins un bloc
            for (int val : matrice[row]) {
                if (val == 1) {
                    if (row + 1 > maxRow) {
                        maxRow = row + 1;
                    }
                    break;
                }
            }
        }
        return maxRow;

    }

    private int calculateTopYSpace() {
        int emptyRows = 0;
    
        // Parcourt les lignes du haut vers le bas
        for (int row = 0; row < matrice.length; row++) {
            boolean isEmpty = true;
            
            // Vérifie si la ligne contient au moins un bloc
            for (int col = 0; col < matrice[row].length; col++) {
                if (matrice[row][col] == 1) {
                    isEmpty = false;
                    break;
                }
            }
            
            if (isEmpty) {
                emptyRows++;
            } else {
                break; // On a trouvé la première ligne non vide
            }
        }
        
        return emptyRows;
    }

    /**
     * Fais pivoter le block de 90°
     */
    public void rotation() {
        int n = matrice.length;
        int[][] nouvelleMatrice = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                nouvelleMatrice[j][n - 1 - i] = matrice[i][j];
            }
        }
        matrice = nouvelleMatrice;
        rightXSpace = calculateRightXSpace();
        leftXSpace = calculateLeftXSpace();
        topXSpace = calculateTopYSpace();
        bottomYSpace = calculateBottomYSpace();
    }

    private void calculateDim(){
        int rows = matrice.length;
        int cols = matrice[0].length;

        int top = rows, bottom = -1;
        int left = cols, right = -1;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (matrice[y][x] != 0) {
                    if (y < top) top = y;
                    if (y > bottom) bottom = y;
                    if (x < left) left = x;
                    if (x > right) right = x;
                }
            }
        }

        width = (right - left + 1);
        height = (bottom - top + 1);
    }

    public int[][] getMatrice(){
        return matrice;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public int getRightXSpace(){
        return rightXSpace; 
    }

    public int getLeftXSpace(){
        return leftXSpace;
    }

    public int getTopYSpace(){
        return topXSpace;
    }

    public int getBottomYSpace(){
        return bottomYSpace;
    }
}