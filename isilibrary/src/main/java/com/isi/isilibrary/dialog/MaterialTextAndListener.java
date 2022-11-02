package com.isi.isilibrary.dialog;

import android.content.DialogInterface;

public class MaterialTextAndListener {

    private final String description;
    private final DialogInterface.OnClickListener clickListener;

    public MaterialTextAndListener(String description, DialogInterface.OnClickListener clickListener) {

        this.description = description;
        this.clickListener = clickListener;
    }

    public DialogInterface.OnClickListener getClickListener() {
        return clickListener;
    }

    public String getDescription() {
        return description;
    }
}
