package scenes;

import com.badlogic.gdx.Screen;
import game.Chess;

public class SceneManager {

    private static SceneManager instance;
    private Chess game;

    private SceneManager() {
        super();
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    // Initialization with the game class
    public void initialize(Chess game) {
        this.game = game;
    }

    // Show in the game the screen which enum type is received
    public void showScreen(SceneEnum sceneEnum, Object... params) {

        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();

        // Show new screen
        AbstractScene newScreen = sceneEnum.getScreen(params);
        newScreen.buildStage();
        game.setScreen(newScreen);

        // Dispose previous screen
        if (currentScreen != null && sceneEnum == SceneEnum.MAIN_MENU) {
            currentScreen.dispose();
        }
    }
}