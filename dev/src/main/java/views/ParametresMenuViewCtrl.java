package views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ParametresMenuViewCtrl implements javafx.fxml.Initializable {

    @FXML
    private ToggleButton musicToggle;

    @FXML
    private ToggleButton soundFxToggle;

    @FXML
    private Button menuButton;

    @FXML
    void onClic(MouseEvent event) {
        if (event.getSource().equals(menuButton)){
            try {
                Stage stage = (Stage) menuButton.getScene().getWindow();
                Font.loadFont(getClass().getResourceAsStream("/fonts/LLPIXEL3.ttf"), 14);
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/MenuView.fxml"));
                MediaManager.attachClickSoundToAllButtons(root);
    
                Scene menuScene = new Scene(root);
                stage.setScene(menuScene);
                stage.sizeToScene();
                stage.show();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        if(event.getSource().equals(soundFxToggle)){
            MediaManager.getInstance().switchFxSound();
        }
        if(event.getSource().equals(musicToggle)){
            MediaManager.getInstance().switchMusicSound();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        musicToggle.setSelected(MediaManager.getInstance().getEnableMusic());
        soundFxToggle.setSelected(MediaManager.getInstance().getEnableFx());
    }

}
