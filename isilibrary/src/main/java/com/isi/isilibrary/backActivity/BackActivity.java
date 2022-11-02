package com.isi.isilibrary.backActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.isi.isilibrary.R;
import com.isi.isilibrary.dialog.Dialog;

import java.util.Objects;

public class BackActivity extends AppCompatActivity {

    private AlertDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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


    public void startLoader(String title){
        final String titleIn = title == null ? "Aggiorno dati..." : title;
        runOnUiThread(() -> pDialog = new Dialog(this).showLoadingDialog(titleIn));

    }

    public void stopLoader(){
        runOnUiThread(() -> {
            if(pDialog != null){
                pDialog.dismiss();
            }
        });
    }
}
