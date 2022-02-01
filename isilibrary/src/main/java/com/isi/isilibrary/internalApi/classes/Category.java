package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;

public class Category extends LastModifiedTables{

    @SerializedName("Id")
    public int id;

    @SerializedName("Name")
    public String name;

    @SerializedName("Color")
    public Integer color;

    public Category(int id, String name, Integer color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
