package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Account {

    @SerializedName("Id")
    public int id;

    @SerializedName("Name")
    public String name;

    @SerializedName("Surname")
    public String surname;

    @SerializedName("Email")
    public String email;

    @SerializedName("Address")
    public String address;

    @SerializedName("Latitude")
    public Float latitude;

    @SerializedName("Longitude")
    public Float longitude;

    @SerializedName("Birthday")
    public Date birthday;

    @SerializedName("Gender")
    public int gender = 0;

    @SerializedName("Pic")
    public String pic;

    @SerializedName("Telephone")
    public String phone;

    @SerializedName("Password")
    public String password;

    @SerializedName("EmailVerification")
    public String email_verification;

    @SerializedName("PhoneVerification")
    public String phone_verification;

    @SerializedName("GeneralVerification")
    public String general_verification;

    @SerializedName("LoginCode")
    public String operator_code = "0000";

    @SerializedName("Nfc")
    public String nfc;

    @SerializedName("Secret")
    public String secret;

    @SerializedName("CtzonCard")
    public String ctzonCard;

}