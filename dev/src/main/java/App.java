import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import views.MediaManager;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Font.loadFont(getClass().getResourceAsStream("/fonts/LLPIXEL3.ttf"), 14);
        MediaManager.getInstance().preloadSounds();

        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/MenuView.fxml"));
            MediaManager.attachClickSoundToAllButtons(root);

            Scene menuScene = new Scene(root);
            primaryStage.setScene(menuScene);
            primaryStage.sizeToScene();
            primaryStage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
