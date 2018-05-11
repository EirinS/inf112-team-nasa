package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameState {

    public enum Message {
        READY,
        OPPONENT_DC,
        OPPONENT_RESIGN,
        UNKNOWN
    }

    @Expose
    @SerializedName("msg")
    String msg;

    @Expose
    @SerializedName("data")
    Object data;

    public Message getMessage() {
        switch (msg) {
            case "ready":
                return Message.READY;
            case "opponent-dc":
                return Message.OPPONENT_DC;
            case "opponent-resign":
                return Message.OPPONENT_RESIGN;
            default:
                return Message.UNKNOWN;
        }
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
