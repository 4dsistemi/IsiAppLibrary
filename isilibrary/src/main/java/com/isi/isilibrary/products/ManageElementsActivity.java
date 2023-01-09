package com.isi.isilibrary.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isi.isiapi.classes.Category;
import com.isi.isiapi.classes.CategoryAndProduct;
import com.isi.isiapi.classes.Product;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;
import com.isi.isilibrary.products.recycler.ElementRecycler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ManageElementsActivity extends BackActivity {

    private CategoryAndProduct categorySelected = null;
    private RecyclerView layout;
    private final List<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_elements);

        setTitle("Gestisci elementi");

        layout = findViewById(R.id.product_list_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        layout.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(() -> {

            products.clear();

            List<CategoryAndProduct> categoryAndProducts = IsiAppActivity.isiCashierRequest.getCategories();

            for (CategoryAndProduct categoryAndProduct : categoryAndProducts){
                products.addAll(categoryAndProduct.product);
            }

            products.sort(Comparator.comparing(one -> one.name));

            runOnUiThread(() -> {

                updateUi("");

                SearchView search = findViewById(R.id.searchElement);

                search.setClickable(true);
                search.setQueryHint("Cerca");

                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {

                        final String s2 = s;

                        runOnUiThread(() -> updateUi(s2));

                        return true;
                    }
                });

            });


        }).start();

    }

    private void updateUi(final String s){

        ElementRecycler recycler;
        if(categorySelected != null) {
            if (categorySelected.category.id == 0) {
                recycler = new ElementRecycler(this, products.stream()
                        .filter(prod -> prod.name.toLowerCase().contains(s.toLowerCase()))
                        .collect(Collectors.toList()));
            } else {
                recycler = new ElementRecycler(this, products.stream()
                        .filter(prod -> prod.name.toLowerCase().contains(s.toLowerCase()) && prod.category_id == categorySelected.category.id)
                        .collect(Collectors.toList()));
            }

            layout.setAdapter(recycler);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_element_menu, menu);

        MenuItem item = menu.findItem(R.id.manage_element_spinner);
        Spinner spinner = (Spinner) item.getActionView();

        new Thread(() -> {
            List<CategoryAndProduct> categories = IsiAppActivity.isiCashierRequest.getCategories();
            CategoryAndProduct cat = new CategoryAndProduct();
            cat.category = new Category(0, "Tutto", 0, "");
            categories.add(0, cat);

            runOnUiThread(() -> {
                ArrayAdapter<CategoryAndProduct> adapter = new ArrayAdapter<>(ManageElementsActivity.this,android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(categories.get(position) != categorySelected){
                            categorySelected = categories.get(position);
                            updateUi("");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            });


        }).start();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.addIntestazioneMenu){
            Intent intent = new Intent(ManageElementsActivity.this, AddManageElementActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
