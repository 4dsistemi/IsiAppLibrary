package com.isi.isilibrary.products;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;
import com.isi.isiapi.classes.CategoryAndProduct;
import com.isi.isiapi.classes.Product;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProductsActivity extends BackActivity {

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porducts);

        setTitle("I miei prodotti");

        recyclerView = findViewById(R.id.productsRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        SearchView spesaSearch = findViewById(R.id.productSearchView);
        spesaSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter != null)
                    adapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Aggiorno Prodotti...");
        pDialog.setCancelable(false);
        pDialog.show();

        new Thread(() -> {

            List<CategoryAndProduct> spese = IsiAppActivity.isiCashierRequest.getCategories();
            List<Product> products = new ArrayList<>();

            for (CategoryAndProduct categoryAndProduct : spese){
                products.addAll(categoryAndProduct.product);
            }

            runOnUiThread(() -> {
                pDialog.dismissWithAnimation();

                if(spese == null){
                    new SweetAlertDialog(ProductsActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Attenzione")
                            .setContentText("Errore di comunicazione con il server. Riprovare")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(sweetAlertDialog -> {
                                sweetAlertDialog.dismissWithAnimation();
                                finish();
                            }).show();
                }else{

                    adapter = new ProductsAdapter(ProductsActivity.this, products);

                    recyclerView.setAdapter(adapter);
                }
            });

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

            Intent i = new Intent(ProductsActivity.this, AddModifyProduct.class);
            startActivity(i);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}