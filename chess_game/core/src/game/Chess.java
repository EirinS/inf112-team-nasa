package game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.chessGame.GameInfo;
import pieces.PieceColor;
import register.Player;
import register.PlayerRegister;
import com.badlogic.gdx.*;
import scenes.SceneEnum;
import scenes.SceneManager;

/**
 * The chess class gets called from the libgdx desktop launcher and  initializes the GUI/Chessgame.
 * It also connects to the playerfile
 */
public class Chess extends Game {
    private SpriteBatch batch;
    private static PlayerRegister playerRegister = new PlayerRegister("playerfile.txt");

    @Override
    public void create() {
        batch = new SpriteBatch();
        SceneManager.getInstance().initialize(this);
        SceneManager.getInstance().showScreen(SceneEnum.MAIN_MENU, this);

        // TODO: 21/03/2018 temp for showing victory screen
        /*SceneManager.getInstance().showScreen(SceneEnum.VICTORY, this,
                new GameInfo(
                        new Player("testSpiller", 1500, 1, 2, 2),
                        new Player("Eirin", 1300, 2, 2, 2),
                        PieceColor.WHITE,
                        GameType.MULTIPLAYER,
                        null
                ), true);
        */
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public SpriteBatch getSpriteBatch() {
        return this.batch;
    }

    public static PlayerRegister getPlayerRegister() {
        return playerRegister;
    }
}
