package com.isi.isilibrary.backActivity;

import android.graphics.Color;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import com.isi.isilibrary.R;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BackActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @CallSuper
    public void updateUI(int layout){
        runOnUiThread(() -> setContentView(layout));
    }


    public void errorPage(int layout){
        runOnUiThread(() -> {
            setContentView(R.layout.error_data);

            Button reloadButton = findViewById(R.id.reload_data_button);

            reloadButton.setOnClickListener(v -> updateUI(layout));
        });

    }

    public void emptyData(){
        runOnUiThread(() -> setContentView(R.layout.empty_data));
    }

    private SweetAlertDialog pDialog;

    public void startLoader(String title){
        final String titleIn = title == null ? "Aggiorno dati..." : title;
        runOnUiThread(() -> {
            pDialog = new SweetAlertDialog(BackActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(titleIn);
            pDialog.setCancelable(false);
            pDialog.show();
        });

    }

    public void stopLoader(){
        runOnUiThread(() -> {
            if(pDialog != null){
                pDialog.dismissWithAnimation();
            }
        });
    }
}
