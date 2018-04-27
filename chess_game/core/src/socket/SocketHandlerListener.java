package socket;

public interface SocketHandlerListener {

    /**
     * Gets called when the socket is connected to the server.
     */
    void onConnected();

    /**
     * Gets called when the socket has joined a game lobby.
     */
    void onJoined();

    /**
     * Gets called when the socket has received data from the server.
     */
    void onData();

    /**
     * Gets called when the socket has received a new state from the server.
     */
    void onState();

    /**
     * Gets called when the socket is disconnected from the server.
     */
    void onDisconnected();
}
