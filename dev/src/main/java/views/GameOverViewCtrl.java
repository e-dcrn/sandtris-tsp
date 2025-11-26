package views;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import controls.GameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameOverViewCtrl {

    @FXML
    private Text scoreText;

    @FXML
    private TextField pseudoField;

    @FXML
    private Button submitButton;

    @FXML
    private Button restartButton;

    @FXML
    private Button quitButton;

    private int score;

    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (restartButton.getScene() != null) {
                MediaManager.attachClickSoundToAllButtons(restartButton.getScene().getRoot());
            }
        });
    }

    @FXML
    void onClick(MouseEvent event) {
        Stage stage = (Stage) restartButton.getScene().getWindow();

        if (event.getSource() == quitButton) {
            try {
                Font.loadFont(getClass().getResourceAsStream("/fonts/LLPIXEL3.ttf"), 14); // facultatif
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/MenuView.fxml"));
                MediaManager.attachClickSoundToAllButtons(root);
                stage.setScene(new Scene(root));
                stage.sizeToScene();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (event.getSource() == restartButton) {
            if (gameController != null) {
                gameController.restartGame(stage);
            } else {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/JeuView.fxml"));
                    MediaManager.attachClickSoundToAllButtons(root);
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (event.getSource() == submitButton) {
            String pseudo = pseudoField.getText().trim();
            if (!pseudo.isEmpty()) {
                saveScore(pseudo, score);
                pseudoField.clear();
                submitButton.setDisable(true);
            }
        }
    }

    public void setScore(int score) {
        this.score = score;
        scoreText.setText("Score: " + score);
    }

    private void saveScore(String pseudo, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("leaderboard.txt", true))) {
            writer.write(pseudo + " : " + score);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
