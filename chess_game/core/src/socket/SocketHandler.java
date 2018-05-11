package socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.socket.client.IO;
import io.socket.client.Socket;
import models.GameState;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;

public class SocketHandler implements ISocketHandler {

    private final String API_URL = "https://team-nasa.herokuapp.com"; // "http://localhost:8080";

    private SocketHandlerListener listener;

    private Socket socket;

    public SocketHandler(SocketHandlerListener listener) {
        this.listener = listener;
        init();
    }

    private void init() {
        try {
            socket = IO.socket(API_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (socket == null) return;

        socket.on(Socket.EVENT_CONNECT, args -> {
            System.out.println("Socket connected.");
            listener.onConnected();

        }).on(SocketEvent.DATA, args -> {

            listener.onData((JSONObject) args[0]);

        }).on(SocketEvent.STATE, args -> {
            GameState state = (new Gson()).fromJson(args[0].toString(), GameState.class);
            listener.onState(state);

        }).on(Socket.EVENT_DISCONNECT, args -> {
            System.out.println("Socket disconnected.");
            listener.onDisconnected();

        });
    }

    @Override
    public void connect() {
        if (socket != null && !socket.connected()) {
            socket.connect();
        }
    }

    @Override
    public void disconnect() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
        }
    }

    @Override
    public void joinGame(String gameId, String playerName) {
        if (socket == null || !socket.connected()) return;
        socket.emit(SocketEvent.JOIN, gameId, playerName);
    }

    @Override
    public void emitData(JSONObject data) {
        if (socket == null || !socket.connected()) return;
        socket.emit(SocketEvent.DATA, data);
    }
}
