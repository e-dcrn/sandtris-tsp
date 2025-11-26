package controls.tetromino;

import java.util.Random;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import models.Form;
import models.SandArea;

public class Tetromino {
    private final Square[] squares;
    private Form form;
    private Color color;
    private double x,y;
    private double xDiff,yDiff;
    private final int squareSize;

    private static final Color[] COULEURS_POSSIBLES = {
        Color.BLUE, Color.ORANGE, Color.GREEN, 
        Color.RED, Color.PURPLE
    };
    
    public Tetromino(int squareSize){
        this.squareSize= squareSize;
        x = 0;
        y = 0;
        form = Form.getFormeAleatoire(squareSize);
        color = COULEURS_POSSIBLES[new Random().nextInt(COULEURS_POSSIBLES.length)];
        squares = new Square[4];
        for(int i = 0; i<4; i++){
            squares[i] = new Square(squareSize, color);
        }
        updateColor();
        updateSquarePosition();
    }

    private void updateSquarePosition(){

        xDiff = form.getWidth()*squareSize/2;
        yDiff = form.getHeight()*squareSize/2;

        int[][] mat = form.getMatrice();
        int k = 0;
        for(int i=0; i<mat.length;i++){
            for(int j=0; j<mat.length;j++){
                if(mat[i][j] == 1){
                    squares[k].setX((j*squareSize + x - xDiff));
                    squares[k].setY((i*squareSize + y - yDiff));
                    k++;
                }
            }
        }
    }

    private void updateColor(){
        for(int i = 0; i<4; i++){
            squares[i].setFill(color);
        }
    }

    public boolean checkFormCollision(SandArea model,double vx, double vy, int particleSize){
        for(int k=0; k<4;k++){
            double X = squares[k].getX() + vx;
            double Y = squares[k].getY() + vy;
            if(model.checkCollision((int)X/particleSize, (int)Y/particleSize)){
                return true;
            }
        }
        return false;
    }

    public void createParticleFromForm(SandArea model, int particleSize){
        for(int k=0; k<4;k++){
            double X = squares[k].getX();
            double Y = squares[k].getY();
            model.createBlock((int)X/particleSize, (int)Y/particleSize, color);
        }
    }

    public void rotation(){
        form.rotation();
        updateSquarePosition();
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setX(double X, double width, double height){
        double minX = 0 - form.getLeftXSpace()*squareSize + xDiff;
        double maxX = width - form.getRightXSpace()*squareSize + xDiff;
        x = Math.max(minX, Math.min(X, maxX));
        updateSquarePosition();
    }

    public void setY(double Y,double width, double height){
        double minY = 0 - form.getTopYSpace()*squareSize + yDiff;
        double maxY = height - form.getBottomYSpace()*squareSize + yDiff;
        y = Math.max(minY, Math.min(Y, maxY));
        updateSquarePosition();
    }

    public void addToRoot(Pane root){
        for(int i = 0; i<4;i++){
            root.getChildren().add(squares[i]);
        }
    }

    public void resetForm(){
        form = Form.getFormeAleatoire(squareSize);
        updateSquarePosition();
        
    }

    public void resetColor() {
        color = COULEURS_POSSIBLES[new Random().nextInt(COULEURS_POSSIBLES.length)];
        updateColor();
    }

    public void setForm(Form newForm){
        this.form = newForm;
        updateSquarePosition();
    }

    public Form getForm(){
        return form;
    }

    public void setColor(Color newColor){
        this.color = newColor;
        updateColor();
    }

    public Color getColor(){
        return color;
    }
}
