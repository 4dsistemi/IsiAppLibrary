package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class LastModifiedTables {

    @SerializedName("LastUpdate")
    public Date last_update = new Date();

    @SerializedName("Modified")
    public int modified = 0;
}
