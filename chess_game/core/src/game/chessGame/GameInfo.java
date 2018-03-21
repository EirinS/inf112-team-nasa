package game.chessGame;

import game.GameType;
import pieces.PieceColor;
import player.AILevel;
import register.Player;

public class GameInfo {

    private PieceColor playerColor;
    private Player player, opponent;
    private GameType gameType;
    private AILevel level;

    public GameInfo(Player player) {
        this.player = player;
        gameType = GameType.SINGLEPLAYER;
    }

    public GameInfo(Player player, Player opponent, PieceColor playerColor, GameType gameType, AILevel level) {
        this.player = player;
        this.opponent = opponent;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
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
