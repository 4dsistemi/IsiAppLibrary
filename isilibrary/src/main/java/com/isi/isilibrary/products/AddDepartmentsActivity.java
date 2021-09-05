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

import com.isi.isiapi.general.classes.Department;
import com.isi.isiapi.general.classes.Product;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddDepartmentsActivity extends BackActivity {

    private Integer product_id = null;
    private Department backDepartment = null;
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

            ArrayList<Department> rates = IsiAppActivity.isiCashierRequest.getDepartment(IsiAppActivity.serial);
            ArrayList<Product> products = IsiAppActivity.isiCashierRequest.getProducts(IsiAppActivity.serial);

            rates.sort(Comparator.comparingInt(departments -> departments.department));

            for (Product p : products){
                names.add(p.name);
            }

            runOnUiThread(() -> {
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

                    for (Department departments : rates){

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
                    final boolean result = IsiAppActivity.isiCashierRequest.modifyDepartment(IsiAppActivity.serial, backDepartment.id, code.getText().toString(),Rates.rates[spinnerRate.getSelectedItemPosition()], product_id);
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
                    final boolean result = IsiAppActivity.isiCashierRequest.addDepartment(IsiAppActivity.serial, Integer.parseInt(code.getText().toString()),Rates.rates[spinnerRate.getSelectedItemPosition()], product_id);
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
