package com.isi.isilibrary.products;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.isi.isiapi.classes.CategoryAndProduct;
import com.isi.isiapi.classes.Product;
import com.isi.isiapi.classes.isicash.IsiCashDepartment;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;
import com.isi.isilibrary.dialog.Dialog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AddDepartmentsActivity extends BackActivity {

    private Integer product_id = null;
    private IsiCashDepartment backDepartment = null;
    private TextInputEditText code;
    private AutoCompleteTextView spinnerRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_departments);

        setTitle("Reparti");

    }

    @Override
    protected void onResume() {
        super.onResume();

        String[] arrayRate = getResources().getStringArray(R.array.rate_percents);

        AutoCompleteTextView spinner = findViewById(R.id.productSpinnerDepartment);
        code = findViewById(R.id.departmentCodeEdit);

        spinnerRate = findViewById(R.id.departmentSpinnerCode);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, arrayRate);
        spinnerRate.setAdapter(adapter2);
        spinnerRate.setText("A - 4%", false);

        List<String> names = new ArrayList<>();
        names.add("Default");

        AlertDialog pDialog = new Dialog(this).showLoadingDialog("Aggiorno reparti...");

        new Thread(() -> {

            List<IsiCashDepartment> rates = IsiAppActivity.isiCashierRequest.getDepartment();
            ArrayList<Product> products = new ArrayList<>();

            List<CategoryAndProduct> categoryAndProducts = IsiAppActivity.isiCashierRequest.getCategories();

            for (CategoryAndProduct cat : categoryAndProducts) {
                products.addAll(cat.product);
            }

            runOnUiThread(() -> {

                pDialog.dismiss();

                if (rates == null) {
                    new Dialog(this).showErrorConnectionDialog(true);
                } else {
                    rates.sort(Comparator.comparingInt(departments -> departments.department));

                    for (Product p : products) {
                        names.add(p.name);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddDepartmentsActivity.this,
                            android.R.layout.simple_spinner_item, names);

                    spinner.setAdapter(adapter);
                    spinner.setOnItemClickListener((adapterView, view, i, l) -> {
                        Log.e("TAG", "onResume: ");
                        if (i > 0) {
                            product_id = products.get(i - 1).id;
                        } else {
                            product_id = null;
                        }
                    });
                    spinner.setText(names.get(0), false);

                    if (getIntent().getBooleanExtra("modify", false)) {

                        for (IsiCashDepartment departments : rates) {

                            if (departments.id == getIntent().getIntExtra("id", -1)) {

                                code.setText(String.format(Locale.getDefault(), "%d", departments.department));

                                backDepartment = departments;

                                for (String s : arrayRate) {
                                    if (s.contains(departments.code + " -")) {
                                        spinnerRate.setText(s, false);
                                    }
                                }

                                if (departments.product_id != null) {
                                    for (int j = 0; j < products.size(); j++) {

                                        if (products.get(j).id == departments.product_id) {

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

            if (backDepartment != null) {
                new Thread(() -> {
                    backDepartment.department = Integer.parseInt(code.getText().toString());
                    backDepartment.code = spinnerRate.getText().toString().split(" - ")[0];
                    backDepartment.product_id = product_id;

                    final boolean result = IsiAppActivity.isiCashierRequest.editDepartment(backDepartment);
                    runOnUiThread(() -> {
                        if (result) {
                            finish();
                            Toast.makeText(AddDepartmentsActivity.this, "Reparto modificato correttamente!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            new Dialog(this).showErrorConnectionDialog(false);
                        }
                    });
                }).start();
            } else {
                new Thread(() -> {
                    IsiCashDepartment department = new IsiCashDepartment(0, Integer.parseInt(code.getText().toString()), product_id, spinnerRate.getText().toString().split(" - ")[0]);
                    final boolean result = IsiAppActivity.isiCashierRequest.addDepartment(department);
                    runOnUiThread(() -> {
                        if (result) {
                            finish();
                            Toast.makeText(AddDepartmentsActivity.this, "Reparto aggiunto correttamente!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            new Dialog(this).showErrorConnectionDialog(false);
                        }

                    });
                }).start();
            }


        }

        return super.onOptionsItemSelected(item);
    }

}
