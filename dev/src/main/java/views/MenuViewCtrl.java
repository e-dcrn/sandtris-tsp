package views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuViewCtrl implements javafx.fxml.Initializable {

    @FXML
    private Slider MusicSlider;

    @FXML
    private Text MusicTitle;

    @FXML
    private Text Title;

    @FXML
    private Button aboutButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button parametresButton;

    @FXML
    private Button startButton;

    @FXML
    void onClic(MouseEvent event) {
        if (event.getSource().equals(startButton)) {
            Stage stage = (Stage) startButton.getScene().getWindow();
            try{
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/JeuView.fxml"));
                MediaManager.attachClickSoundToAllButtons(root);
                
                Scene menuScene = new Scene(root);
                stage.setScene(menuScene);
                stage.sizeToScene();
                stage.show();
            }
            catch(IOException e){
                e.printStackTrace();
                return;
            }
        }
        if (event.getSource().equals(parametresButton)) {
            Stage stage = (Stage) startButton.getScene().getWindow();
            try{
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/ParametresMenuView.fxml"));
                MediaManager.attachClickSoundToAllButtons(root);
                
                Scene menuScene = new Scene(root);
                stage.setScene(menuScene);
                stage.sizeToScene();
                stage.show();
            }
            catch(IOException e){
                e.printStackTrace();
                return;
            }
        }
        if (event.getSource().equals(exitButton)){
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.close();
        }
        if (event.getSource().equals(aboutButton)){
            Stage stage = (Stage) aboutButton.getScene().getWindow();

        }
        if (event.getSource().equals(leaderboardButton)) {
            Stage stage = (Stage) leaderboardButton.getScene().getWindow();
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/LeaderboardView.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MediaManager instance = MediaManager.getInstance();
        instance.setMusicPlayer();
        MusicSlider.setValue(instance.getVolume()*20);
        MusicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            instance.setVolume(newVal.doubleValue()/20);
        });
    }
}

