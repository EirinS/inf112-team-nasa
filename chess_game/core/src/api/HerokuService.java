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
     * @param opponentUid Player unique ID
     * @param opponentName Player name
     * @param opponentColor Player starting color
     * @param opponentRating Player rating
     * @return Response from server, check ApiResponse class for docs.
     */
    /*
    @FormUrlEncoded
    @POST("/create")
    Call<ApiResponse> createGame(
            @Field("name") String name,
            @Field("type") GameType type,
            @Field("opponentUid") String opponentUid,
            @Field("opponentName") String opponentName,
            @Field("opponentColor") PieceColor opponentColor,
            @Field("opponentRating") int opponentRating
    );*/

    /**
     * Requests creation of new multiplayer game given MultiplayerGame.
     * @param game MultiplayerGame to create
     * @return Response from server, check ApiResponse class for docs.
     */
    @POST("/create")
    Call<ApiResponse> createGame(@Body MultiplayerGame game);

    /**
     * Requests joining a game.
     * @param id Game ID
     * @param uid Player unique id
     * @param name Player name
     * @return Response from server, check ApiResponse class for docs.
     */
    @FormUrlEncoded
    @POST("/join")
    Call<ApiResponse> joinGame(
            @Field("id") String id,
            @Field("uid") String uid,
            @Field("name") String name
    );
}
