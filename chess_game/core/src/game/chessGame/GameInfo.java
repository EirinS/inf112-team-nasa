package game.chessGame;

import game.GameType;
import pieces.PieceColor;
import player.AILevel;

public class GameInfo {

    private PieceColor playerColor;
    private String playerName, opponentName;
    private GameType gameType;
    private AILevel level;

    public GameInfo(String playerName) {
        this.playerName = playerName;
        opponentName = "Computer";
        gameType = GameType.SINGLEPLAYER;
    }

    public GameInfo(String playerName, String opponentName, PieceColor playerColor, GameType gameType, AILevel level) {
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.playerColor = playerColor;
        this.gameType = gameType;
        this.level = level;
    }

    public PieceColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PieceColor playerColor) {
        this.playerColor = playerColor;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public AILevel getLevel() {
        return level;
    }

    public void setLevel(AILevel level) {
        this.level = level;
    }
}
