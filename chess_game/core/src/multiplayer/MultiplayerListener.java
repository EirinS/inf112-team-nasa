package multiplayer;

import models.MultiplayerGame;

import java.util.List;

public interface MultiplayerListener {

    /**
     * Gets called when the games have returned from API.
     * @param games List of games
     */
    void gamesListed(List<MultiplayerGame> games);

    /**
     * Gets called when a game has been created.
     */
    void gameCreated(MultiplayerGame game);

    /**
     * Gets called when a game has been joined.
     */
    void gameJoined(MultiplayerGame game);

    /**
     * Gets called when there is an error from API.
     */
    void error(Throwable t);

    /**
     * Gets called when there is an unexpected error from API.
     */
    void unexpectedError();
}
