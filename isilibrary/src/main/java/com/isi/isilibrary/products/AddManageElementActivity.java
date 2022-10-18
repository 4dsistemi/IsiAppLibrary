package com.isi.isilibrary.products;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddManageElementActivity extends BackActivity {

    private Product products;
    private List<Ingredients> ingredients = null;
    private int categoryDef = 0;
    private int productDef = 0;
    private int department = 1;
    private AppCompatImageView imageView;
    private List<CategoryAndProduct> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manage_element);

        Intent back = getIntent();

        this.products = new Gson().fromJson(back.getStringExtra("product"), Product.class);

        imageView = findViewById(R.id.imageChoosenElement);

        new Thread(() -> {
            categories = IsiAppActivity.isiCashierRequest.getCategories();

            runOnUiThread(this::updateUI);

        }).start();

    }

    public void updateUI(){

        final TextView nametext = findViewById(R.id.modifyName);
        final TextView priceText = findViewById(R.id.priceModify);
        final TextView priceBancoText = findViewById(R.id.priceModifyBanco);
        final TextView barcodeText = findViewById(R.id.barcodeElement);
        final TextView descriptionElement = findViewById(R.id.descriptionElement);
        final AutoCompleteTextView categrySpinner = findViewById(R.id.addManageElementSpinner);
        final AutoCompleteTextView productSpinner = findViewById(R.id.product_connection_spinner);
        final AutoCompleteTextView departmentSpinner = findViewById(R.id.departmentCodeElement);
        final MaterialButton modify = findViewById(R.id.modifyElementButton);
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
            if(products.image != null){

                try{

                    new Thread(() -> {

                        Bitmap bitmap = getBitmapFromURL(products.image);

                        if(bitmap != null){
                            runOnUiThread(() -> imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 100, false)));
                        }

                    }).start();

                }catch (Exception ignored){

                }

            }

            categoryDef = products.category_id;

            setTitle("Modifica elemento");
            nametext.setText(products.name);
            priceText.setText(String.format(Locale.getDefault(), "%.2f", products.price));
            priceBancoText.setText(String.format(Locale.getDefault(), "%.2f", products.price_banco));
            barcodeText.setText(products.barcode_value);

            if(products.color != 0 && products.color != -1)
                chooseColor.setBackgroundColor(products.color);
            modify.setTag(0);

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

            modify.setText(R.string.add);
            modify.setTag(1);
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

        modify.setOnClickListener(view -> {

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

                    if(modify.getTag().equals(1)){

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


        });

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
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 50){

            try{
                products.image = data.getStringExtra("image");

                new Thread(() -> {

                    Bitmap bitmap = getBitmapFromURL(products.image);

                    runOnUiThread(() -> imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 100, false)));

                }).start();

            }catch (Exception ignored){

            }

        }else if(requestCode == 888){
            String ingredients = data.getStringExtra("ingredients");

            this.ingredients = new Gson().fromJson(ingredients, new TypeToken<ArrayList<Ingredients>>(){}.getType());

        }

    }

    public Bitmap getBitmapFromURL(String src) {

        String urlString = "http://www.ctzon.it/foodImages/productImages/" + src;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
