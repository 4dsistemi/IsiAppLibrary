package com.isi.isilibrary.internalApi.classes;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Commercial extends LastModifiedTables{

    @SerializedName("LocalId")
    @NonNull
    public String local_id = "";

    @SerializedName("Creation")
    public Date creation;

    @SerializedName("FirstActivation")
    public Date first_activation;

    @SerializedName("LastActivation")
    public Date last_activation;

    @SerializedName("Points")
    public int points;

    @SerializedName("Promo24Week")
    public int promo_24_week;

    @SerializedName("Latitude")
    public double latitude;

    @SerializedName("Longitude")
    public double longitude;

    @SerializedName("Logo")
    public String logo;

    @SerializedName("IsOnCityzon")
    public int is_on_cityzon;

    @SerializedName("Name")
    public String name;

    @SerializedName("Society")
    public String society;

    @SerializedName("Address")
    public String address;

    @SerializedName("Phone")
    public String phone;

    @SerializedName("Iva")
    public String iva;

    @SerializedName("Website")
    public String website;

    @SerializedName("Cap")
    public String cap;

}
