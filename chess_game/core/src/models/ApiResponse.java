package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @Expose
    @SerializedName("status")
    String status;

    @Expose
    @SerializedName("error")
    String error;

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccessful() {
        return error == null;
    }
}
