package com.isi.isilibrary.response;

import android.app.Activity;

import com.isi.isilibrary.R;

public class ResponseEmpty {

    private final Activity activity;

    public ResponseEmpty(Activity activity) {
        this.activity = activity;
    }

    public void addEmptyPage(){
        activity.setContentView(R.layout.empty_data);
    }
}
