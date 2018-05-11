package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
/**
 * 
 * An abstract class responsible for the graphical user interface. 
 * It allows the screen classes to draw upon the same Stage and implement all the methods that a screen
 * should implement. The Stage is responsible for drawing the graphical elements (also known as actors)
 * to the screen. This class is currently inherited by the three classes MainMenuScene,
 *  GameScene and VictoryScene.
 *
 */
public abstract class AbstractScene extends Stage implements Screen {

    protected AbstractScene() {
        super(new ScreenViewport());
    }

    protected boolean built;
    public abstract void buildStage();

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Calling to Stage methods
        super.act(delta);
        super.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}