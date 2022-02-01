package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;

public class ApplicationList {

    @SerializedName("Id")
    public int id;
    @SerializedName("Name")
    public String name;
    public String Package;
    @SerializedName("Description")
    public String Description;
    @SerializedName("Ut")
    public int ut;
    @SerializedName("Canone")
    public int canone;

}