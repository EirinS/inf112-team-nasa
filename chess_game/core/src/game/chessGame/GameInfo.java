package game.chessGame;

import models.MultiplayerGame;
import pieces.PieceColor;
import player.AILevel;
import db.Player;

public class GameInfo {

    private PieceColor playerColor;
    private Player player, opponent;
    private GameType gameType;
    private AILevel level;
    private boolean singlePlayer;
    private String gameOverString;
    private String playerRatingChange, opponentRatingChange;

    private boolean online;
    private MultiplayerGame multiplayerGame;

    public GameInfo(Player player) {
        this.player = player;
        gameType = GameType.REGULAR;
    }

    public GameInfo(Player player, Player opponent, PieceColor playerColor, GameType gameType, AILevel level, boolean singlePlayer, boolean online, MultiplayerGame multiplayerGame) {
        this.player = player;
        this.opponent = opponent;
        this.playerColor = playerColor;
        this.gameType = gameType;
        this.level = level;
        this.singlePlayer = singlePlayer;
        this.online = online;
        this.multiplayerGame = multiplayerGame;
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
    
    public void setSinglePlayer(boolean singleplayer) {
    	this.singlePlayer = singleplayer;
    }
    
    public boolean isSinglePlayer() {
    	return singlePlayer;
    }
    
    public String getGameOverString()
    {
    	return gameOverString;
    }
    
    public void setGameOverString(String gameOverString)
    {
    	this.gameOverString = gameOverString;
    }
    
    public void setOpponentRatingChange(int change) {
   	 if (change > 0) {
		 opponentRatingChange = "(+" + change + ")";
     } else if (change < 0) {
    	 opponentRatingChange = "(" + change + ")";
     } else{
    	 opponentRatingChange = "(+0)";
     }
    }
    
    public void setPlayerRatingChange(int change) {
      	 if (change > 0) {
      		playerRatingChange = "(+" + change + ")";
        } else if (change < 0) {
        	playerRatingChange = "(" + change + ")";
        } else{
        	playerRatingChange = "(+0)";
        }
       }
    
    public String getPlayerRatingChange()
    {
    	return playerRatingChange;
    }

    public String getOpponentRatingChange()
    {
    	return opponentRatingChange;
    }

    public void setIsOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }

    public MultiplayerGame getMultiplayerGame() {
        return multiplayerGame;
    }

    public void setMultiplayerGame(MultiplayerGame game) {
        this.multiplayerGame = game;
    }
}
