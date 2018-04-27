package models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultiplayerGame {

    class Opponent {

        @Expose
        @SerializedName("uid")
        String uid;

        @Expose
        @SerializedName("name")
        String name;

        @Expose
        @SerializedName("color")
        String color;

        @Expose
        @SerializedName("rating")
        Integer rating;

        @Override
        public String toString() {
            return "Opponent{" +
                    "uid='" + uid + '\'' +
                    ", name='" + name + '\'' +
                    ", color='" + color + '\'' +
                    ", rating=" + rating +
                    '}';
        }
    }

    @Expose
    @SerializedName("name")
    String name;

    @Expose
    @SerializedName("type")
    String type;

    @Expose
    @SerializedName("opponent")
    Opponent opponent;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Opponent getOpponent() {
        return opponent;
    }

    @Override
    public String toString() {
        return "MultiplayerGame{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", opponent=" + opponent +
                '}';
    }
}