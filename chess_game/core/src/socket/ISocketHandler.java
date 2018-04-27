package socket;

public interface ISocketHandler {

    void connect();

    void joinGame(String gameId);

    void emitData();
}
