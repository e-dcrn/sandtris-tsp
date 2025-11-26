package views;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MediaManager {

    private static MediaManager instance;

    private double volume = 0.5;

    private MediaPlayer musicPlayer;

    private MediaPlayer fxPlayer;
    private Media destructionMedia;
    private Media groundCollisionMedia;
    private boolean isFxPlaying = false;
    
    private Media clickSoundMedia;
    private MediaPlayer clickSoundPlayer;
    private boolean isClickPlaying = false;

    private boolean enableFx = true;
    private boolean enableMusic = true;

    private boolean soundPreloaded = false;

    private MediaManager() {
    }

    public static MediaManager getInstance() {
        if (instance == null) {
            instance = new MediaManager();
        }
        return instance;
    }

    public double getVolume() {
        return volume;
    }

    public void switchFxSound(){
        enableFx = !enableFx;
        reloadVolume();
    }

    public void switchMusicSound(){
        enableMusic = !enableMusic;
        reloadVolume();
    }

    public boolean getEnableMusic(){
        return enableMusic;
    }

    public boolean getEnableFx(){
        return enableFx;
    }

    private void reloadVolume(){
        try {
            musicPlayer.setVolume(volume*(enableMusic?1:0));
            fxPlayer.setVolume(volume*(enableFx?1:0));
            clickSoundPlayer.setVolume(volume*(enableFx?1:0));
        }
        catch (Exception e) {
        }
    }

    public void setVolume(double volume) {
        this.volume = volume;
        reloadVolume();
    }

    public void setMusicPlayer() {
        if (musicPlayer == null) {
            try {
                String musicFile = getClass().getResource("/mp3/made_in_abyss_drums.wav").toExternalForm();
                Media media = new Media(musicFile);
                musicPlayer = new MediaPlayer(media);
                musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                musicPlayer.setVolume(volume*(enableMusic?1:0));
                musicPlayer.play();
            } catch (Exception e) {
                System.err.println("Erreur lors de la lecture du média : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void preloadSounds() {
        if (!soundPreloaded) {
            try {
                String destructionPath = getClass().getResource("/mp3/mario_hit.wav").toExternalForm();
                destructionMedia = new Media(destructionPath);


                String collisionPath = getClass().getResource("/mp3/minecraft_placing_block.wav").toExternalForm();
                groundCollisionMedia = new Media(collisionPath);
                
                String clickPath = getClass().getResource("/mp3/menu_clic_sound.wav").toExternalForm();
                clickSoundMedia = new Media(clickPath);
                
                soundPreloaded = true;
            } catch (Exception e) {
                System.err.println("Erreur lors du préchargement des médias : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void playMediaSound(Media media) {
        if (media == null || isFxPlaying || !enableFx) return;
        fxPlayer = new MediaPlayer(media);
        fxPlayer.setVolume(volume*(enableFx?1:0));
        fxPlayer.setOnEndOfMedia(() -> {
            fxPlayer.dispose();
            isFxPlaying = false;
        });
        isFxPlaying = true;
        fxPlayer.play();
    }

    public void playGroundCollisionSound() {
        playMediaSound(groundCollisionMedia);
    }

    public void playDestructionSound(){
        playMediaSound(destructionMedia);
    }

    public void playClickSound() {
        if (groundCollisionMedia == null || isClickPlaying || !enableFx) return;
        clickSoundPlayer = new MediaPlayer(clickSoundMedia);
        clickSoundPlayer.setVolume(volume*(enableFx?1:0));
        clickSoundPlayer.setOnEndOfMedia(() -> {
            clickSoundPlayer.dispose();
            isClickPlaying = false;
        });
        isClickPlaying = true;
        clickSoundPlayer.play();
    }

    public static void attachClickSoundToAllButtons(Parent root) {
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                EventHandler<javafx.event.ActionEvent> originalHandler = button.getOnAction();
                button.setOnAction(event -> {
                    MediaManager.getInstance().playClickSound();
                    if (originalHandler != null) {
                        originalHandler.handle(event);
                    }
                });
            } else if (node instanceof Parent) {
                // Appel récursif pour les sous-conteneurs (VBox, HBox, GridPane, etc.)
                attachClickSoundToAllButtons((Parent) node);
            }
        }
    }

}
