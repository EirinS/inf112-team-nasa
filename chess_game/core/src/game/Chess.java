package game;

import com.badlogic.gdx.*;
import db.Database;
import db.IDatabase;
import scenes.SceneEnum;
import scenes.SceneManager;

/**
 * The chess class gets called from the libgdx desktop launcher and initializes the GUI/Chessgame.
 * It also connects to the playerfile
 */
public class Chess extends Game {

    private static IDatabase database;

    @Override
    public void create() {
        database = new Database();
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

    public static IDatabase getDatabase() {
        return database;
    }
}
