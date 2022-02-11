package com.isi.isilibrary.products;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.isi.isiapi.classes.isicash.IsiCashDepartment;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;
import com.isi.isiapi.classes.CategoryAndProduct;
import com.isi.isiapi.classes.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddDepartmentsActivity extends BackActivity {

    private Integer product_id = null;
    private IsiCashDepartment backDepartment = null;
    private EditText code;
    private Spinner spinnerRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_departments);

        setTitle("Reparti");

    }

    @Override
    protected void onResume() {
        super.onResume();

        Spinner spinner = findViewById(R.id.productSpinnerDepartment);
        code = findViewById(R.id.departmentCodeEdit);

        spinnerRate = findViewById(R.id.departmentSpinnerCode);

        List<String> names = new ArrayList<>();

        names.add("Default");

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Aggiorno Reparti...");
        pDialog.setCancelable(false);
        pDialog.show();

        new Thread(() -> {

            List<IsiCashDepartment> rates = IsiAppActivity.isiCashierRequest.getDepartment();
            ArrayList<Product> products = new ArrayList<>();

            List<CategoryAndProduct> categoryAndProducts = IsiAppActivity.isiCashierRequest.getCategories();

            for (CategoryAndProduct cat : categoryAndProducts){
                products.addAll(cat.product);
            }

            runOnUiThread(() -> {

                pDialog.dismissWithAnimation();

                if(rates == null){
                    new SweetAlertDialog(AddDepartmentsActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Attenzione")
                            .setContentText("Errore di comunicazione con il server. Riprovare")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(sweetAlertDialog -> {
                                sweetAlertDialog.dismissWithAnimation();
                                finish();
                            }).show();
                }else{
                    rates.sort(Comparator.comparingInt(departments -> departments.department));

                    for (Product p : products){
                        names.add(p.name);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddDepartmentsActivity.this,
                            android.R.layout.simple_spinner_item, names);

                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if(i > 0){
                                product_id = products.get(i -1).id;
                            }else{
                                product_id = null;
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    if(getIntent().getBooleanExtra("modify", false)){

                        for (IsiCashDepartment departments : rates){

                            if(departments.id == getIntent().getIntExtra("id", -1)){

                                code.setText(String.format(Locale.getDefault(), "%d", departments.department));

                                backDepartment = departments;

                                String[] arrayRate = getResources().getStringArray(R.array.rate_percents);

                                for (int j = 0; j < arrayRate.length; j++) {

                                    if(arrayRate[j].contains(departments.code + " -")){
                                        spinnerRate.setSelection(j, true);
                                    }

                                }

                                if(departments.product_id != null){
                                    for (int j = 0; j < products.size(); j++) {

                                        if(products.get(j).id == departments.product_id){

                                            spinner.setSelection(j + 1);

                                        }

                                    }
                                }

                            }

                        }

                    }

                }

            });

        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_intestazione_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.addIntestazioneDone) {

            if(backDepartment != null){
                new Thread(() -> {
                    backDepartment.department = Integer.parseInt(code.getText().toString());
                    backDepartment.code = Rates.rates[spinnerRate.getSelectedItemPosition()];
                    backDepartment.product_id = product_id;

                    final boolean result = IsiAppActivity.isiCashierRequest.editDepartment(backDepartment);
                    runOnUiThread(() -> {
                        if (result) {
                            finish();
                            Toast.makeText(AddDepartmentsActivity.this, "Reparto modificato correttamente!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            new SweetAlertDialog(AddDepartmentsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Attenzione")
                                    .setContentText("Errore di comunicazione con il server. Riprovare")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation).show();                        }
                    });
                }).start();
            }else{
                new Thread(() -> {

                    IsiCashDepartment department = new IsiCashDepartment(0, Integer.parseInt(code.getText().toString()), product_id, Rates.rates[spinnerRate.getSelectedItemPosition()]);
                    final boolean result = IsiAppActivity.isiCashierRequest.addDepartment(department);
                    runOnUiThread(() -> {
                        if (result) {
                            finish();
                            Toast.makeText(AddDepartmentsActivity.this, "Reparto aggiunto correttamente!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            new SweetAlertDialog(AddDepartmentsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Attenzione")
                                    .setContentText("Errore di comunicazione con il server. Riprovare")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation).show();                        }
                    });
                }).start();
            }


        }

        return super.onOptionsItemSelected(item);
    }

}
