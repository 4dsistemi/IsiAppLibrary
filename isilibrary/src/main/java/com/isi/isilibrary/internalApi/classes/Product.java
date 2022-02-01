package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;

public class Product extends LastModifiedTables{

    @SerializedName("Id")
    public int id;

    @SerializedName("Name")
    public String name;

    @SerializedName("Price")
    public float price;

    @SerializedName("Color")
    public Integer color;

    @SerializedName("Deparment")
    public int department;

    @SerializedName("CategoryId")
    public int category_id;

    @SerializedName("BarcodeValue")
    public String barcode_value;

    @SerializedName("Active")
    public int active = 1;

    public Product(String name, float price, int department, String barcode_value, Integer color, int category_id) {
        this.name = name;
        this.price = price;
        this.department = department;
        this.barcode_value = barcode_value;
        this.color = color;
        this.category_id = category_id;
    }

}
