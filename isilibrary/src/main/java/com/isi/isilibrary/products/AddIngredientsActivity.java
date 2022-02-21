package com.isi.isilibrary.products;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.isi.isiapi.classes.Ingredients;
import com.isi.isiapi.classes.isimaga.ProductForniture;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddIngredientsActivity extends BackActivity {

    private LinearLayout ingredientsLayout;
    private ArrayList<Ingredients> ingredientsAdd;
    private String search = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        setTitle("Aggiungi ingredienti");

        ingredientsAdd = new ArrayList<>();

        updateUI(R.layout.activity_add_ingredients);

    }

    @Override
    public void updateUI(int layoutOut) {
        super.updateUI(layoutOut);

        ingredientsLayout = findViewById(R.id.addIngredientsLayout);

        final SearchView searchView = findViewById(R.id.searchIngredients);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search = s;
                updateUI(layoutOut);
                return true;
            }
        });

        Button done = findViewById(R.id.doneAddIngredients);

        done.setOnClickListener(view -> onBackPressed());

        ingredientsLayout.removeAllViews();

        new Thread(() -> {

            List<ProductForniture> storage = IsiAppActivity.isiCashierRequest.isimagaGetProductForniture();

            if(storage == null){
                errorPage(layoutOut);
            }else{
                for(final ProductForniture ingredient : storage){

                    runOnUiThread(() -> {

                        if (ingredient.name.toLowerCase().contains(search.toLowerCase())) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                            View inflate = inflater.inflate(R.layout.add_ingredients_layout, ingredientsLayout, false);
                            final TextView text = inflate.findViewById(R.id.nameElementText);
                            Button plus = inflate.findViewById(R.id.buttonPlusLayout);

                            text.setText(ingredient.name);

                            ingredientsLayout.addView(inflate);

                            plus.setOnClickListener(view -> {

                                if (plus.getText().equals("+")) {

                                    LayoutInflater inflaterPerso = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                                    assert inflaterPerso != null;
                                    @SuppressLint("InflateParams") final View inflatePerso = inflaterPerso.inflate(R.layout.quantity_cell, null);
                                    EditText quantity = inflatePerso.findViewById(R.id.quantity_cell_edit);

                                    new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                                            .setTitleText("Aggiungi quantità in " + IsiAppActivity.isiCashierRequest.transformIsimagaUnity(ingredient.unity_id))
                                            .setConfirmText("Ok")
                                            .setHideKeyBoardOnDismiss(true)
                                            .setCustomView(inflatePerso)
                                            .setConfirmClickListener(sDialog -> {

                                                sDialog.dismissWithAnimation();

                                                try {

                                                    Ingredients i = new Ingredients(ingredient.id, 0, (Float.parseFloat(quantity.getText().toString())));

                                                    ingredientsAdd.add(i);

                                                    plus.setText("-");

                                                } catch (Exception e) {

                                                    Toast.makeText(AddIngredientsActivity.this, "Formato non corretto", Toast.LENGTH_SHORT).show();

                                                }

                                                sDialog.dismissWithAnimation();
                                            })
                                            .show();



                                } else {

                                    for (Ingredients i : ingredientsAdd) {
                                        if (i.product_forniture_id == ingredient.id) {

                                            ingredientsAdd.remove(i);
                                            break;

                                        }
                                    }
                                    plus.setText("+");

                                }

                            });
                        }

                    });

                }
            }

        }).start();

    }

    @Override
    public void onBackPressed() {

        Intent resultIntent = new Intent();

        Gson gson = new Gson();

        resultIntent.putExtra("ingredients", gson.toJson(ingredientsAdd));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }
}
