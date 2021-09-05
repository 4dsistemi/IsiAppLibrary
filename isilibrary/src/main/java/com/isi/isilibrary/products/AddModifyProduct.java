package com.isi.isilibrary.products;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.gson.Gson;
import com.isi.isiapi.general.classes.Category;
import com.isi.isiapi.general.classes.Department;
import com.isi.isiapi.general.classes.Product;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddModifyProduct extends BackActivity {

    private int color = -1;
    private EditText barcodeEdit;
    private Product backProduct = null;
    private Button chooseColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_modify_product);

        Intent back = getIntent();
        if(back.getStringExtra("product") != null){
            backProduct = new Gson().fromJson(back.getStringExtra("product"), Product.class);
        }

        chooseColor = findViewById(R.id.chooseColorProduct);
        barcodeEdit = findViewById(R.id.barcodeValueText);

        barcodeEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                barcodeEdit.setText("");
            }
        });

        chooseColor.setOnClickListener(v -> ColorPickerDialogBuilder
                .with(AddModifyProduct.this)
                .setTitle("Scegli colore")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                    color = selectedColor;
                    chooseColor.setBackgroundColor(color);
                })
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {

                })
                .setNegativeButton("cancella", (dialog, which) -> color = -1)
                .build()
                .show());

    }

    @Override
    protected void onResume() {
        super.onResume();

        final EditText name = findViewById(R.id.productNameEdit);
        final EditText price = findViewById(R.id.priceProductEdit);
        final Spinner categorySpinner = findViewById(R.id.categorySpinnerAddProduct);

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Aggiorno Prodotti...");
        pDialog.setCancelable(false);
        pDialog.show();

        new Thread(() -> {
            final List<Category> cats = IsiAppActivity.isiCashierRequest.getCategories(IsiAppActivity.serial);
            final List<Department> rates = IsiAppActivity.isiCashierRequest.getDepartment(IsiAppActivity.serial);

            runOnUiThread(() -> {

                pDialog.dismissWithAnimation();

                if (cats.size() == 0) {

                    new SweetAlertDialog(AddModifyProduct.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Attenzione")
                            .setContentText("Prima di inserire un prodotto si necessita almeno di una categoria creata")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(sweetAlertDialog -> {
                                sweetAlertDialog.dismissWithAnimation();
                                finish();
                            }).show();

                } else {
                    String[] catsAdapter = new String[cats.size()];

                    for (int i = 0; i < cats.size(); i++) {

                        catsAdapter[i] = cats.get(i).name;

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddModifyProduct.this,
                            android.R.layout.simple_spinner_item, catsAdapter);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);

                    Button addProduct = findViewById(R.id.addProductButton);

                    String[] adapterString = new String[rates.size()];

                    for (int i = 0; i < rates.size(); i++) {

                        adapterString[i] = String.format(Locale.getDefault(), "REP %d - %s", rates.get(i).department, Rates.getRatesValor(rates.get(i).code));

                    }

                    final Spinner rateSpinner = findViewById(R.id.rateSpinner);

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(AddModifyProduct.this,
                            android.R.layout.simple_spinner_item, adapterString);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    rateSpinner.setAdapter(adapter2);

                    addProduct.setOnClickListener(v -> {

                        SweetAlertDialog loader = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                        loader.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        loader.setTitleText("Aggiorno Prodotti...");
                        loader.setCancelable(false);
                        loader.show();

                        int catId;

                        catId = cats.get(categorySpinner.getSelectedItemPosition()).id;

                        final int finalCatId = catId;
                        new Thread(() -> {
                            try {
                                float priceFloat = Float.parseFloat(price.getText().toString().replace(",", "."));
                                boolean res;
                                if (backProduct != null) {
                                    res = IsiAppActivity.isiCashierRequest.modifyProduct(new Product(backProduct.id, name.getText().toString(), priceFloat, rates.get(rateSpinner.getSelectedItemPosition()).id, barcodeEdit.getText().toString(), color, finalCatId));
                                } else {
                                    res = IsiAppActivity.isiCashierRequest.addProduct(IsiAppActivity.serial, new Product(-1, name.getText().toString(), priceFloat, rates.get(rateSpinner.getSelectedItemPosition()).id, barcodeEdit.getText().toString(), color, finalCatId));
                                }
                                loader.dismissWithAnimation();
                                if (!res) {
                                    new SweetAlertDialog(AddModifyProduct.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Attenzione")
                                            .setContentText("Errore di comunicazione con il server. Riprovare")
                                            .setConfirmText("Ok")
                                            .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation).show();
                                } else {
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                loader.dismissWithAnimation();
                                new SweetAlertDialog(AddModifyProduct.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Attenzione")
                                        .setContentText("Prezzo non valido")
                                        .setConfirmText("Ok")
                                        .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation).show();
                            }
                        }).start();
                    });

                    if (backProduct != null) {

                        name.setText(backProduct.name);

                        for (int i = 0; i < cats.size(); i++) {

                            if (cats.get(i).id == backProduct.category_id) {

                                categorySpinner.setSelection(i);
                                break;

                            }

                        }

                        price.setText(String.format(Locale.getDefault(), "%.2f", backProduct.price));

                        for (int i = 0; i < rates.size(); i++) {

                            if (rates.get(i).id == backProduct.department_id) {

                                rateSpinner.setSelection(i);
                                break;

                            }

                        }

                        chooseColor.setBackgroundColor(backProduct.color);

                        color = backProduct.color;

                        addProduct.setText(R.string.modify);

                        barcodeEdit.setText(backProduct.barcode.equals("null") ? "" : backProduct.barcode);
                    }

                }

            });

        }).start();
    }
}