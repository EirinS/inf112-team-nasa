package multiplayer;

import models.MultiplayerGame;

public interface IMultiplayer {

    /**
     * Sends a request to list all games.
     */
    void listGames();

    /**
     * Sends a request to create a game
     * @param game Game to create
     */
    void createGame(MultiplayerGame game);

    /**
     * Sends a request to join a specific game (by gameId)
     * @param gameId Game id to join
     * @param playerUid Player unique id
     * @param playerName Player name
     */
    void joinGame(String gameId, String playerUid, String playerName);
}
