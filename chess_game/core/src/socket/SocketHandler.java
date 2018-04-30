package socket;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.Arrays;

public class SocketHandler implements ISocketHandler {

    private final String API_URL = "https://team-nasa.herokuapp.com";

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
            System.out.println("Data received: " + Arrays.toString(args));

            // TODO: 25.04.2018 do something with data
            listener.onData();

        }).on(SocketEvent.STATE, args -> {
            System.out.println("State received: " + Arrays.toString(args));

            // TODO: 25.04.2018 do something with state
            listener.onState();

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
    public void joinGame(String gameId) {
        if (socket == null || !socket.connected()) return;
        socket.emit(SocketEvent.JOIN, gameId);
    }

    @Override
    public void emitData() {
        if (socket == null || !socket.connected()) return;
        // TODO: 25.04.2018 emit data here
        // socket.emit(SocketEvent.DATA, data);
    }
}
