package com.isi.isilibrary.products;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isi.isiapi.general.classes.Category;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CategoryProductActivity extends BackActivity {

    private LinearLayout categoryLayout;

    ArrayList<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);

        setTitle("Le tue categorie");

        categoryLayout = findViewById(R.id.categoryProductLayout);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.intestazione_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addIntestazioneMenu) {

            Intent i = new Intent(CategoryProductActivity.this, AddCategoryActivity.class);
            startActivity(i);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateData();
    }

    public void updateData() {
        SweetAlertDialog loader = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loader.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        loader.setTitleText("Aggiorno Categorie...");
        loader.setCancelable(false);
        loader.show();

        new Thread(() -> {
            categories = IsiAppActivity.isiCashierRequest.getCategories(IsiAppActivity.serial);
            runOnUiThread(() -> {
                updateUI();
                loader.dismissWithAnimation();
            });
        }).start();
    }


    public void updateUI() {

        categoryLayout.removeAllViews();


        for (Category category : categories){

            TextView view = new TextView(categoryLayout.getContext());

            view.setText(category.name);

            view.setTextSize(20);

            categoryLayout.addView(view);
        }

    }
}
