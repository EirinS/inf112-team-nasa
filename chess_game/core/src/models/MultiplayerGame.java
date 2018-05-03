package models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import pieces.PieceColor;

public class MultiplayerGame {

    public class Player {

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
            return "Player{" +
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
    @SerializedName("player")
    Player player;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return name + " - " + type + " - " + player.name + " - " + player.rating;
    }
}