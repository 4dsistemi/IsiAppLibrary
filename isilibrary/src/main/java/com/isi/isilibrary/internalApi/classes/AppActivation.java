package com.isi.isilibrary.internalApi.classes;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AppActivation extends LastModifiedTables{

    @SerializedName("Id")
    public int id;

    @SerializedName("ApplicationId")
    public int application_id;

    @SerializedName("Priority")
    public Integer priority;

    @SerializedName("ActivationTime")
    public Date activation_time;

    @SerializedName("PositionInMenu")
    public int position_in_menu;

    @SerializedName("Active")
    public int active;

    public AppActivation(int application_id, Integer priority, Date activation_time, int position_in_menu, int active) {
        this.application_id = application_id;
        this.priority = priority;
        this.activation_time = activation_time;
        this.position_in_menu = position_in_menu;
        this.active = active;
        modified = 1;
    }
}
