package multiplayer;

import api.HerokuService;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import models.ApiResponse;
import models.MultiplayerGame;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Multiplayer implements IMultiplayer {

    private final String API_URL = "https://team-nasa.herokuapp.com";
    private MultiplayerListener listener;

    private HerokuService service;
    private Socket socket;

    public Multiplayer(MultiplayerListener listener) {
        this.listener = listener;
        initService();
        initSocket();

    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(HerokuService.class);
    }

    private void initSocket() {
        try {
            socket = IO.socket(API_URL);
            socket.on(Socket.EVENT_CONNECT, args -> {
                System.out.println("Socket connected.");
            }).on(Socket.EVENT_DISCONNECT, args -> {
                System.out.println("Socket disconnected.");
            });

            // TODO: 25/04/2018 figure out where to connect socket
            //socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void listGames() {
        service.listGames().enqueue(new Callback<List<MultiplayerGame>>() {

            @Override
            public void onResponse(Call<List<MultiplayerGame>> call, Response<List<MultiplayerGame>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.gamesListed(response.body());
                } else {
                    listener.unexpectedError();
                }
            }

            @Override
            public void onFailure(Call<List<MultiplayerGame>> call, Throwable t) {
                t.printStackTrace();
                listener.error(t);
            }
        });
    }

    @Override
    public void createGame(MultiplayerGame game) {
        service.createGame(game).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccessful() && response.body().getStatus().equals("ok")) {
                        listener.gameJoined();
                    } else {
                        listener.error(new Throwable(response.body().getError()));
                    }
                } else {
                    listener.unexpectedError();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
                listener.error(t);
            }
        });
    }

    @Override
    public void joinGame(String gameId, String playerUid, String playerName) {
        service.joinGame(gameId, playerUid, playerName).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccessful() && response.body().getStatus().equals("ok")) {
                        listener.gameCreated();
                    } else {
                        listener.error(new Throwable(response.body().getError()));
                    }
                } else {
                    listener.unexpectedError();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                t.printStackTrace();
                listener.error(t);
            }
        });
    }
}