package com.isi.isilibrary.internalApi.classes;

import com.google.gson.annotations.SerializedName;

public class IsiCashDepartment extends LastModifiedTables{

    @SerializedName("Id")
    public int id;

    @SerializedName("DepartmentNumber")
    public int department;

    @SerializedName("DepartmentCode")
    public String code;

    @SerializedName("ProductId")
    public Integer product_id;

    public IsiCashDepartment(int id, int department, Integer product_id, String code) {
        this.id = id;
        this.department = department;
        this.code = code;
        this.product_id = product_id;
    }

}
