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

    public MultiplayerGame(String name, String type, String opponentUid, String opponentName, String opponentColor, Integer opponentRating) {
        this.name = name;
        this.type = type;
        this.opponentUid = opponentUid;
        this.opponentName = opponentName;
        this.opponentColor = opponentColor;
        this.opponentRating = opponentRating;
    }

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