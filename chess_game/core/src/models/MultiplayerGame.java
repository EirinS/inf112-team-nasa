package models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultiplayerGame {

    @Expose
    @SerializedName("name")
    String name;

    @Expose
    @SerializedName("type")
    String type;

    @Expose
    @SerializedName("opponentUid")
    String opponentUid;

    @Expose
    @SerializedName("opponentName")
    String opponentName;

    @Expose
    @SerializedName("opponentColor")
    String opponentColor;

    @Expose
    @SerializedName("opponentRating")
    Integer opponentRating;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getOpponentUid() {
        return opponentUid;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getOpponentColor() {
        return opponentColor;
    }

    public Integer getOpponentRating() {
        return opponentRating;
    }
}