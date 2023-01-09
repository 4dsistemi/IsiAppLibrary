package com.isi.isilibrary.products;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isi.isiapi.classes.CategoryAndProduct;
import com.isi.isiapi.classes.Ingredients;
import com.isi.isiapi.classes.Product;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddManageElementActivity extends BackActivity {

    private Product products;
    private List<Ingredients> ingredients = null;
    private int categoryDef = 0;
    private int productDef = 0;
    private int department = 1;

    private TextView nametext;
    private TextView priceText;
    private TextView priceBancoText;
    private TextView barcodeText;
    private TextView descriptionElement;
    private AutoCompleteTextView categrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manage_element);

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI(R.layout.activity_add_manage_element);
    }

    @Override
    public void updateUI(int layout) {
        super.updateUI(layout);
        
        Intent back = getIntent();

        products = new Gson().fromJson(back.getStringExtra("product"), Product.class);

        new Thread(() -> {

            List<CategoryAndProduct> categories = IsiAppActivity.isiCashierRequest.getCategories();

            if(categories != null){
                runOnUiThread(() -> {
                    this.nametext = findViewById(R.id.modifyName);
                    this.priceText = findViewById(R.id.priceModify);
                    this.priceBancoText = findViewById(R.id.priceModifyBanco);
                    this.barcodeText = findViewById(R.id.barcodeElement);
                    this.descriptionElement = findViewById(R.id.descriptionElement);
                    this. categrySpinner = findViewById(R.id.addManageElementSpinner);
                    AutoCompleteTextView productSpinner = findViewById(R.id.product_connection_spinner);
                    AutoCompleteTextView departmentSpinner = findViewById(R.id.departmentCodeElement);
                    final MaterialButton chooseColor = findViewById(R.id.productColorButton);

                    final ArrayAdapter<CategoryAndProduct> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    List<Product> productsArray = new ArrayList<>();
                    Product fake = new Product("Nessuno", 0, 0, 0, "", 0, 0, "", "", 0);
                    fake.id = 0;
                    productsArray.add(0, fake);
                    for (CategoryAndProduct cat: categories) {
                        productsArray.addAll(cat.product);
                    }

                    final ArrayAdapter<Product> adapterProduct = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, productsArray);
                    adapterProduct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    final ArrayAdapter<Integer> adapterDepartment = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, new Integer[]{1, 2, 3, 4});
                    adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    categrySpinner.setAdapter(adapter);
                    productSpinner.setAdapter(adapterProduct);
                    departmentSpinner.setAdapter(adapterDepartment);


                    if(products != null){

                        categoryDef = products.category_id;

                        setTitle("Modifica elemento");
                        nametext.setText(products.name);
                        priceText.setText(String.format(Locale.getDefault(), "%.2f", products.price));
                        priceBancoText.setText(String.format(Locale.getDefault(), "%.2f", products.price_banco));
                        barcodeText.setText(products.barcode_value);

                        if(products.color != 0 && products.color != -1)
                            chooseColor.setBackgroundColor(products.color);

                        for(CategoryAndProduct cat : categories){
                            if(cat.category.id == products.category_id){
                                categoryDef = cat.category.id;
                                categrySpinner.setText(cat.category.name, false);
                                break;
                            }
                        }

                        for(Product product : productsArray){
                            if(products.connection_product != null){
                                if(product.id == products.connection_product){
                                    productSpinner.setText(product.name, false);
                                    productDef = product.id;
                                    break;

                                }
                            }

                        }

                        department = products.department;
                        descriptionElement.setText(products.description);
                        departmentSpinner.setText(String.format(Locale.getDefault(), "%d", products.department), false);



                    }
                    else{
                        setTitle("Aggiungi elemento");
                        productSpinner.setText("Nessuno", false);
                        departmentSpinner.setText("1", false);

                        try{
                            products = new Product("", 0, 0, 1, "", 0, 0, "", "", 0);
                        }catch (Exception e){

                            new AlertDialog.Builder(AddManageElementActivity.this)
                                    .setTitle("Attenzione")
                                    .setMessage("Devi prima creare una categoria")
                                    .setCancelable(true)
                                    .setNegativeButton("OK", (dialogInterface, i) -> finish()).show();

                        }
                    }

                    categrySpinner.setOnItemClickListener((parent, view, position, id) -> categoryDef = categories.get(position).category.id);

                    productSpinner.setOnItemClickListener((parent, view, position, id) -> productDef = productsArray.get(position).id);

                    departmentSpinner.setOnItemClickListener((parent, view, position, id) -> department = position + 1);

                    final Button addIngredients = findViewById(R.id.addIngredientsButton);

                    addIngredients.setOnClickListener(view -> {
                        Intent i = new Intent(AddManageElementActivity.this, AddIngredientsActivity.class);
                        startActivityForResult(i, 888);
                    });

                    chooseColor.setOnClickListener(v -> ColorPickerDialogBuilder
                            .with(AddManageElementActivity.this)
                            .setTitle("Scegli colore")
                            .initialColor(Color.WHITE)
                            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                            .density(12)
                            .setOnColorSelectedListener(selectedColor -> {
                                products.color = selectedColor;
                                chooseColor.setBackgroundColor(selectedColor);
                            })
                            .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {

                            })
                            .setNegativeButton("cancella", (dialog, which) -> products.color = 0)
                            .build()
                            .show());
                });
            }
            else{
                errorPage(layout);
            }
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
            nametext.setError(null);

            try{

                if(nametext.getText().toString().equals("") || nametext.getText().toString().contains("-") || nametext.getText().toString().contains(":") || nametext.getText().toString().contains("!") || nametext.getText().toString().contains("#") || nametext.getText().toString().contains(",")){
                    nametext.setError("Il nome non può contenere caratteri speciali o essere vuoto");
                }else if(categoryDef == 0){
                    categrySpinner.setError("Scegli una categoria");
                }else{

                    String price1 = priceText.getText().toString().replace(",",".");
                    String price2 = priceBancoText.getText().toString().replace(",",".");

                    final float priceDouble = Float.parseFloat(price1);
                    final float priceBancoDouble = Float.parseFloat(price2);

                    products.price = priceDouble;
                    products.price_banco = priceBancoDouble;
                    products.category_id = categoryDef;
                    products.department = department;
                    products.connection_product = (productDef != 0) ? productDef : null;
                    products.name = nametext.getText().toString();
                    products.barcode_value = barcodeText.getText().toString();
                    products.description = descriptionElement.getText().toString();

                    if(products == null){

                        new Thread(() -> {

                            if(IsiAppActivity.isiCashierRequest.addProduct(products, ingredients)){
                                runOnUiThread(this::finish);
                            }

                        }).start();

                    }else{

                        new Thread(() -> {

                            if(IsiAppActivity.isiCashierRequest.editProduct(products, ingredients))
                                runOnUiThread(this::finish);

                        }).start();

                        finish();
                    }
                }

            }catch (Exception ignore){}

        }
        
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 888){
            String ingredients = data.getStringExtra("ingredients");

            this.ingredients = new Gson().fromJson(ingredients, new TypeToken<ArrayList<Ingredients>>(){}.getType());

        }

    }

}
