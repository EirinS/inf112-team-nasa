package db;

import register.Player;

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
     * Updates the player in the database given a playername.
     * @param playerName Player to look for
     * @param rating New rating
     * @param wins New wins
     * @param losses New losses
     * @param draws New draws
     * @return True if ok, false if error during updating.
     * @throws SQLException if database error.
     */
    boolean updatePlayer(String playerName, int rating, int wins, int losses, int draws) throws SQLException;
}
