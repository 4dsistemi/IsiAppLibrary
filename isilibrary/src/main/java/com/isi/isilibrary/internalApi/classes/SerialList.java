package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SerialList {

    @SerializedName("SerialValue")
    public String serial_value;

    @SerializedName("CommercialId")
    public String commercial_id;

    @SerializedName("LastUpdate")
    public Date last_update;

    @SerializedName("DatabaseVersion")
    public int database_version;

}
