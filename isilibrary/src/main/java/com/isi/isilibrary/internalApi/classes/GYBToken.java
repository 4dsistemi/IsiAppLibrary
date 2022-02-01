package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;

public class GYBToken extends LastModifiedTables{

    @SerializedName("Id")
    public int id;

    @SerializedName("RefreshToken")
    public String refresh_token;

    public GYBToken(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
