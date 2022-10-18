package com.isi.isilibrary.products;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isi.isiapi.classes.isicash.IsiCashDepartment;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;
import com.isi.isiapi.classes.CategoryAndProduct;
import com.isi.isiapi.classes.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DepartmentActivity extends BackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        setTitle("I tuoi reparti");

    }

    @Override
    protected void onResume() {
        super.onResume();

        final LinearLayout departmentLayout = findViewById(R.id.departmentLayout);

        departmentLayout.removeAllViews();

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Aggiorno Reparti...");
        pDialog.setCancelable(false);
        pDialog.show();

        new Thread(() -> {

            List<IsiCashDepartment> rates = IsiAppActivity.isiCashierRequest.getDepartment();
            List<CategoryAndProduct> products = IsiAppActivity.isiCashierRequest.getCategories();

            if(rates == null || products == null){
                runOnUiThread(() -> new SweetAlertDialog(DepartmentActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Attenzione")
                        .setContentText("Errore di comunicazione con il server. Riprovare")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }).show());
            }else{
                rates.sort(Comparator.comparingInt(departments -> departments.department));

                for (final IsiCashDepartment rate : rates){

                    LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    assert inflater != null;
                    @SuppressLint("InflateParams") final View inflate = inflater.inflate(R.layout.department_cell, departmentLayout, false);

                    TextView name = inflate.findViewById(R.id.nameDepartmentCellText);

                    name.setText(String.format(Locale.getDefault(), "Reparto %d a %s", rate.department, Rates.getRatesValor(rate.code)));

                    TextView description = inflate.findViewById(R.id.descriptionRateTextCell);

                    if(rate.product_id != null){
                        for(CategoryAndProduct categoryAndProduct : products){
                            for (Product prod : categoryAndProduct.product){
                                if(prod.id == rate.product_id){
                                    description.setText(String.format("Descrizione %s", prod.name));
                                }
                            }
                        }


                    }

                    Button modify = inflate.findViewById(R.id.modifyDepartmentButton);

                    modify.setOnClickListener(view -> runOnUiThread(() -> {
                        Intent i = new Intent(DepartmentActivity.this, AddDepartmentsActivity.class);
                        i.putExtra("modify", true);
                        i.putExtra("id", rate.id);
                        startActivity(i);
                    }));

                    runOnUiThread(() -> departmentLayout.addView(inflate));

                }

                runOnUiThread(pDialog::dismissWithAnimation);
            }


        }).start();
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

            Intent i = new Intent(DepartmentActivity.this, AddDepartmentsActivity.class);
            startActivity(i);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}