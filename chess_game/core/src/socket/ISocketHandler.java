package socket;

import org.json.JSONObject;

public interface ISocketHandler {

    /**
     * Connect to Heroku server.
     */
    void connect();

    /**
     * Disconnect from Heroku server.
     */
    void disconnect();

    /**
     * Join game through sockets.
     * @param gameId Game ID to join
     * @param playerName Player name
     */
    void joinGame(String gameId, String playerName);

    /**
     * Emit data to server.
     */
    void emitData(JSONObject data);
}
