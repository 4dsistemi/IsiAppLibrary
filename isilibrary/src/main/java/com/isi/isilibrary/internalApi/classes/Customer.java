package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;

public class Customer extends LastModifiedTables {

    @SerializedName("Id")
    public int id;
    @SerializedName("Name")
    public String name;
    @SerializedName("Surname")
    public String surname;
    @SerializedName("Iva")
    public String iva;
    @SerializedName("Email")
    public String email;
    @SerializedName("Address")
    public String address;
    @SerializedName("City")
    public String city;
    @SerializedName("Province")
    public String province;
    @SerializedName("Zip")
    public String zip;
    @SerializedName("Country")
    public String country;
    @SerializedName("Phone")
    public String phone;
    @SerializedName("Pec")
    public String pec;
    @SerializedName("AeCode")
    public String aeCode;
    @SerializedName("Birthday")
    public String birthday;
    @SerializedName("Society")
    public String society;
    @SerializedName("Fiscal")
    public String fiscal;
    @SerializedName("Birthplace")
    public String birthplace;
    @SerializedName("Gender")
    public int gender;
    @SerializedName("DocType")
    public String docType;
    @SerializedName("DocSerial")
    public String docSerial;
    @SerializedName("DocRelease")
    public String docRelease;
    @SerializedName("DocExpire")
    public String docExpire;
    @SerializedName("CommercialComunication")
    public boolean commercialComunication;
    @SerializedName("Active")
    public int active = 1;

    public Customer(String name, String surname, String iva, String email, String address, String city, String province, String zip, String country, String phone, String pec, String aeCode, String birthday, String society, String fiscal, boolean commercialComunication) {
        this.name = name;
        this.surname = surname;
        this.iva = iva;
        this.email = email;
        this.address = address;
        this.city = city;
        this.province = province;
        this.zip = zip;
        this.country = country;
        this.phone = phone;
        this.pec = pec;
        this.aeCode = aeCode;
        this.birthday = birthday;
        this.society = society;
        this.fiscal = fiscal;
        this.commercialComunication = commercialComunication;
    }
}