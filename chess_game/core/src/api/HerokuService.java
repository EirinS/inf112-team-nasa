package api;

import game.chessGame.GameType;
import models.ApiResponse;
import models.MultiplayerGame;
import pieces.PieceColor;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Service to communicate between Express API (hosted at Heroku) using Retrofit.
 * @author Jonas Triki
 */
public interface HerokuService {

    /**
     * Requests list of all multiplayer games available.
     * @return All multiplayer games.
     */
    @GET("/list")
    Call<List<MultiplayerGame>> listGames();

    /**
     * Requests creation of new multiplayer game given parameters.
     * @param name Game name
     * @param type Game type
     * @param playerName Player name
     * @param playerColor Player starting color
     * @param playerRating Player rating
     * @return Response from server, check ApiResponse class for docs.
     */
    @FormUrlEncoded
    @POST("/create")
    Call<ApiResponse> createGame(
            @Field("name") String name,
            @Field("type") String type,
            @Field("playerName") String playerName,
            @Field("playerColor") String playerColor,
            @Field("playerRating") int playerRating
    );

    /**
     * Requests joining a game.
     * @param id Game ID
     * @return Response from server, check ApiResponse class for docs.
     */
    @FormUrlEncoded
    @POST("/join")
    Call<ApiResponse> joinGame(
            @Field("id") String id
    );
}
