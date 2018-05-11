package multiplayer;

import game.chessGame.GameType;
import models.MultiplayerGame;
import pieces.PieceColor;

public interface IMultiplayer {

    /**
     * Sends a request to list all games.
     */
    void listGames();

    /**
     * Sends a request to create a game
     * @param name Game lobby name
     * @param gameType Game type
     * @param opponentName Player name
     * @param opponentColor Player piece color
     * @param opponentRating Player rating
     */
    void createGame(String name, GameType gameType,
                    String opponentName, PieceColor opponentColor, int opponentRating);

    /**
     * Sends a request to join a specific game (by gameId)
     * @param gameId Game id to join
     */
    void joinGame(String gameId);

    /**
     * If we are currently loading games from server.
     * @return True if we are currently loading games from server, false otherwhise.
     */
    boolean isListingGames();
}
