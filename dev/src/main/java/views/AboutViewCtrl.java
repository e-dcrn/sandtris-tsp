package views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AboutViewCtrl {

    @FXML
    private Pane ControlesPane;

    @FXML
    private Button ExitButton;

    @FXML
    private Pane MadeByPane;

    @FXML
    private Pane NotAffiliatedPane;

    @FXML
    private Pane RulesPane;

    @FXML
    private Pane TitlePane;

    @FXML
    void onClic(MouseEvent event) {
        if (event.getSource().equals(ExitButton)) {
            try {
                Stage stage = (Stage) ExitButton.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/MenuView.fxml"));
                MediaManager.attachClickSoundToAllButtons(root);
    
                Scene menuScene = new Scene(root);
                stage.setScene(menuScene);
                stage.sizeToScene();
                stage.show();
            }
            catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

}
