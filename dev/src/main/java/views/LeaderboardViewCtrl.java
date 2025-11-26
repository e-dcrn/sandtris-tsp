package views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class LeaderboardViewCtrl {

    @FXML
    private ListView<String> scoreList;

    @FXML
    public void initialize() {
        List<String> scores = readScores();
        scoreList.getItems().addAll(scores);

        // Ajout du son aux boutons une fois la scène chargée
        Platform.runLater(() -> {
            if (scoreList.getScene() != null) {
                MediaManager.attachClickSoundToAllButtons(scoreList.getScene().getRoot());
            }
        });
    }

    private List<String> readScores() {
        List<String> lines = new ArrayList<>();
        File file = new File("leaderboard.txt");

        if (!file.exists()) return lines;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.lines()
                .map(String::trim)
                .filter(l -> l.contains(":"))
                .sorted((a, b) -> {
                    int scoreA = Integer.parseInt(a.split(":")[1].trim());
                    int scoreB = Integer.parseInt(b.split(":")[1].trim());
                    return Integer.compare(scoreB, scoreA);
                })
                .limit(10)
                .forEach(lines::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @FXML
    private void onReturnClick(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/MenuView.fxml"));
            MediaManager.attachClickSoundToAllButtons(root);
            Stage stage = (Stage) scoreList.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onResetClick(MouseEvent event) {
        clearScores();
        scoreList.getItems().clear();
    }

    private void clearScores() {
        File file = new File("leaderboard.txt");
        try (PrintWriter writer = new PrintWriter(file)) {
            // Vide le fichier
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
