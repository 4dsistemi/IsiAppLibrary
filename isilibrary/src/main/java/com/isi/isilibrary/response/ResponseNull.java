package com.isi.isilibrary.response;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.isi.isilibrary.R;

public class ResponseNull {

    private final Activity activity;
    private final View.OnClickListener onReloadClickListener;

    public ResponseNull(@NonNull Activity activity, View.OnClickListener onReloadClickListener) {
        this.activity = activity;
        this.onReloadClickListener = onReloadClickListener;
    }

    public void addErrorPage(){
        activity.setContentView(R.layout.error_data);

        Button reloadButton = activity.findViewById(R.id.reload_data_button);

        if(onReloadClickListener != null)
            reloadButton.setOnClickListener(onReloadClickListener);
    }
}
