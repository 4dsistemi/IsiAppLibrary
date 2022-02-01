package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;


public class Fattura extends LastModifiedTables{

    @SerializedName("Id")
    public int id;
    @SerializedName("Billid")
    public int bill_id;
    @SerializedName("Cutomer")
    public String customer;
    @SerializedName("RecoverCode")
    public String recover_code;
    @SerializedName("Datetime")
    public String datetime;
    @SerializedName("Status")
    public int status;
    @SerializedName("FatturaDeleted")
    public int deleted;
    @SerializedName("IncrementalNumber")
    public int incremental_number;

    public Fattura(int bill_id, String customer, String recover_code, String datetime, int status, int deleted, int incremental_number) {
        this.bill_id = bill_id;
        this.customer = customer;
        this.recover_code = recover_code;
        this.datetime = datetime;
        this.status = status;
        this.deleted = deleted;
        this.incremental_number = incremental_number;
    }
}
