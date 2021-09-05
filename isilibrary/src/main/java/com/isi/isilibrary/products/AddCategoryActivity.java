package com.isi.isilibrary.products;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddCategoryActivity extends BackActivity {

    private Integer color = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        final Button chooseColor = findViewById(R.id.chooseCategoryColorButton);

        chooseColor.setOnClickListener(v -> ColorPickerDialogBuilder
                .with(AddCategoryActivity.this)
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                    color = selectedColor;
                    chooseColor.setBackgroundColor(color);
                })
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {

                })
                .setNegativeButton("cancel", (dialog, which) -> color = null)
                .build()
                .show());
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

            EditText editText = findViewById(R.id.categoryNameEdit);

            final String name = editText.getText().toString();

            if(!name.equals("")){

                SweetAlertDialog loader = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                loader.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                loader.setTitleText("Aggiorno Categoria...");
                loader.setCancelable(false);
                loader.show();
                new Thread(() -> {

                    final boolean result = IsiAppActivity.isiCashierRequest.addCategory(IsiAppActivity.serial, name, color);
                    runOnUiThread(() -> {
                        loader.dismissWithAnimation();
                        if (result) {
                            finish();
                            Toast.makeText(AddCategoryActivity.this, "Categoria aggiunta correttamente!",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            new SweetAlertDialog(AddCategoryActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Attenzione")
                                    .setContentText("Errore di comunicazione con il server. Riprovare")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation).show();
                        }
                    });
                }).start();


                finish();
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
