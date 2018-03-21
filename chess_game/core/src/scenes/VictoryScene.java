package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import game.Chess;
import game.WindowInformation;

public class VictoryScene extends AbstractScene {

    private Chess game;
    private Skin skin;

    public VictoryScene(Chess game) {
        this.game = game;
    }

    @Override
    public void buildStage() {

        // Set-up stage
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.txt"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);

        Image imgBackground = new Image(new Texture("board/game_bg.png"));
        imgBackground.setSize(WindowInformation.WIDTH, WindowInformation.HEIGHT);
        addActor(imgBackground);
    }
}
