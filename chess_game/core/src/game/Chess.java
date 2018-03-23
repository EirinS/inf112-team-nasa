package game;

import register.PlayerRegister;
import com.badlogic.gdx.*;
import scenes.SceneEnum;
import scenes.SceneManager;

/**
 * The chess class gets called from the libgdx desktop launcher and initializes the GUI/Chessgame.
 * It also connects to the playerfile
 */
public class Chess extends Game {
    private static PlayerRegister playerRegister = new PlayerRegister("playerfile.txt");

    @Override
    public void create() {
        SceneManager.getInstance().initialize(this);
        SceneManager.getInstance().showScreen(SceneEnum.MAIN_MENU, this);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }

    public static PlayerRegister getPlayerRegister() {
        return playerRegister;
    }
}
