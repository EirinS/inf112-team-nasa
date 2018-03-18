package game;

import boardstructure.Square;
import com.badlogic.gdx.graphics.Texture;
import sprites.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;

public class GameInfo {

    private PlayerColor playerColor;
    private String playerName, opponentName;
    private HashMap<String, Texture> sprites;
    private ArrayList<Square> squares;

    public GameInfo(PlayerColor playerColor, String playerName, String opponentName, HashMap<String, Texture> sprites, ArrayList<Square> squares) {
        this.playerColor = playerColor;
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.sprites = sprites;
        this.squares = squares;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public HashMap<String, Texture> getSprites() {
        return sprites;
    }

    public void setSprites(HashMap<String, Texture> sprites) {
        this.sprites = sprites;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }
}
