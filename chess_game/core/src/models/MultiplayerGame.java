package models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import pieces.PieceColor;

public class MultiplayerGame {

    public class Opponent {

        @Expose
        @SerializedName("name")
        String name;

        @Expose
        @SerializedName("color")
        String color;

        @Expose
        @SerializedName("rating")
        Integer rating;

        public String getName() {
            return name;
        }

        public PieceColor getColor() {
            return color.equals("w") ? PieceColor.WHITE : PieceColor.BLACK;
        }

        public Integer getRating() {
            return rating;
        }

        @Override
        public String toString() {
            return "Opponent{" +
                    "name='" + name + '\'' +
                    ", color='" + color + '\'' +
                    ", rating=" + rating +
                    '}';
        }
    }

    @Expose
    @SerializedName("_id")
    String id;

    @Expose
    @SerializedName("name")
    String name;

    @Expose
    @SerializedName("type")
    String type;

    @Expose
    @SerializedName("opponent")
    Opponent opponent;

    public String getId() {
        return id;
    }

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
        return name + " - " + type + " - " + opponent.name + " - " + opponent.rating;
    }
}