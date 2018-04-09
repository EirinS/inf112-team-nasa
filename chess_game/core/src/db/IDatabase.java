package db;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IDatabase {

    /**
     * Get's all the players from the database.
     * @return Players from database.
     * @throws SQLException if database error.
     */
    ArrayList<Player> listPlayers() throws SQLException;

    /**
     * Registers the player in the database
     * @param player Player to write to the database.
     * @return True if ok, false if already exists.
     * @throws SQLException if database error.
     */
    boolean registerPlayer(Player player) throws SQLException;

    /**
     * Gets a player given a playerName
     * @param playerName Name of the player
     * @return Player, null if error.
     * @throws SQLException if database error.
     */
    Player getPlayer(String playerName) throws SQLException;

    /**
     * Checks if a player is registered with a given name.
     * @param playerName Player name to lookup
     * @return True if player is registered, false otherwhise.
     */
    boolean isPlayerRegistered(String playerName);

    /**
     * Updates the player in the database given a playername.
     * @param playerName Player to look for
     * @param rating New rating
     * @param win_lose_draw 0 = win, 1 = lose, 2 = draw
     * @return True if ok, false if error during updating.
     * @throws SQLException if database error.
     */
    boolean updatePlayer(String playerName, int rating, int win_lose_draw) throws SQLException;
}
