package socket;

import models.GameState;
import org.json.JSONObject;

public interface SocketHandlerListener {

    /**
     * Gets called when the socket is connected to the server.
     */
    void onConnected();

    /**
     * Gets called when the socket has received data from the server.
     */
    void onData(JSONObject data);

    /**
     * Gets called when the socket has received a new state from the server.
     */
    void onState(GameState state);

    /**
     * Gets called when the socket is disconnected from the server.
     */
    void onDisconnected();
}
