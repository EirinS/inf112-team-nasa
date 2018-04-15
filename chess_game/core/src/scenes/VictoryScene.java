package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import game.Chess;
import game.WindowInformation;
import game.chessGame.GameInfo;

import db.Player;
import sprites.SquareTextureLoader;
import styling.Colors;

import java.sql.SQLException;

public class VictoryScene extends AbstractScene {

    private Chess game;
    private GameInfo gameInfo;
    private Boolean playerWon;

    public VictoryScene(Chess game, GameInfo gameInfo, Boolean playerWon) {
        this.game = game;
        this.gameInfo = gameInfo;
        this.playerWon = playerWon;
    }

    private String ratingChange(String playerName) {
        try {
            int diff = Chess.getDatabase().getPlayer(playerName).getRating() - gameInfo.getPlayer().getRating();
            if (diff > 0) {
                return "(+" + diff + ")";
            } else if (diff < 0) {
                return "(" + diff + ")";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String playerStats(Player player) {
        return player.getName()+ "\n" +
                player.getWins() + "W/" + player.getDraws() + "D/" + player.getLosses() + "L" + "\n\n" +
                "Rating: " + player.getRating() + ratingChange(player.getName());
    }

    @Override
    public void buildStage() {

        // Set-up stage
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skin/uiskin.txt"));
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"), atlas);
        skin.getFont("font-title").getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Texture lineTexture = SquareTextureLoader.createSquare((int) getWidth() - 100, 2, Color.WHITE);

        Image imgBackground = new Image(new Texture("board/game_bg.png"));
        imgBackground.setSize(WindowInformation.WIDTH, WindowInformation.HEIGHT);
        addActor(imgBackground);

        String titleStr;
        if (playerWon == null) {
            titleStr = "Draw, " + gameInfo.getGameOverString() + ", " + gameInfo.getPlayer().getName() + "!";
        } else {
            titleStr = playerWon ? "Victory, " + gameInfo.getGameOverString() + ", " + gameInfo.getPlayer().getName() + "!" : "Defeat, " + gameInfo.getGameOverString() + ", " + gameInfo.getPlayer().getName() + "!";
        }

        Label title = new Label(titleStr, skin, "title-plain");
        title.setFontScale(1.5f);
        title.setPosition((getWidth() - title.getWidth()) / 2, getHeight() - title.getHeight() - 100);
        addActor(title);

        Image line = new Image(lineTexture);
        line.setPosition(50, title.getY() - 30);
        addActor(line);

        Label player = new Label(playerStats(gameInfo.getPlayer()) , skin, "title-plain");
        if (playerWon != null && playerWon) player.setColor(Colors.turnColor);
        player.setFontScale(1.5f);
        player.setPosition(line.getX() + 25, line.getY() - player.getHeight() - 40);
        addActor(player);

        String opponentName;
        if (gameInfo.getOpponent() == null) {
            opponentName = "Computer" + "\n\n" + gameInfo.getLevel().toString();
        } else {
            opponentName = playerStats(gameInfo.getOpponent());
        }

        Label opponent = new Label(opponentName, skin, "title-plain");
        if (playerWon != null && !playerWon) opponent.setColor(Colors.turnColor);
        opponent.setAlignment(Align.right);
        opponent.setFontScale(1.5f);
        opponent.setPosition(line.getX() + line.getWidth() - opponent.getWidth(), line.getY() - opponent.getHeight() - 40);
        addActor(opponent);

        line = new Image(lineTexture);
        line.setPosition(50, player.getY() - 50);
        addActor(line);

        TextButton backBtn = new TextButton("Back to main menu", skin);
        backBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().showScreen(SceneEnum.MAIN_MENU, game);
            }
        });
        backBtn.setSize(backBtn.getWidth() * 2, backBtn.getHeight() * 2);
        backBtn.setPosition((getWidth() - backBtn.getWidth()) / 2, line.getY() - backBtn.getHeight() - 25);
        addActor(backBtn);
    }
}
