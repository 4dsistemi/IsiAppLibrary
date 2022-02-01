package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;

public class FiscalPrinter extends LastModifiedTables{

    @SerializedName("Id")
    public int id ;

    @SerializedName("Name")
    public String name;

    @SerializedName("Ip")
    public String ip;

    @SerializedName("Matricola")
    public String matricola;

    @SerializedName("Type")
    public int type = 0;

    public FiscalPrinter(int id, String name, String ip, String matricola, int type) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.matricola = matricola;
        this.type = type;
    }
}
