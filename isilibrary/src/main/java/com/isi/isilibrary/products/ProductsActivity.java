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
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.isi.isiapi.general.classes.Product;

import java.util.List;

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

            List<Product> spese = IsiAppActivity.isiCashierRequest.getProducts(IsiAppActivity.serial);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            pDialog.dismissWithAnimation();

            runOnUiThread(() -> {
                adapter = new ProductsAdapter(ProductsActivity.this, spese);

                recyclerView.setAdapter(adapter);
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