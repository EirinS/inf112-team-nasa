package socket;

public interface ISocketHandler {

    void connect();

    void disconnect();

    void joinGame(String gameId);

    void emitData();
}
