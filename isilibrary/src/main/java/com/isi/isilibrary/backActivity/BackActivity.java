package com.isi.isilibrary.backActivity;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

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
}
