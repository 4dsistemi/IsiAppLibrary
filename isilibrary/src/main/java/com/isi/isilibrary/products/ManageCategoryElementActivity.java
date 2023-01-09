package com.isi.isilibrary.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.isi.isiapi.classes.CategoryAndProduct;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;

import java.util.List;

public class ManageCategoryElementActivity extends BackActivity {

    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category_element);

        setTitle("Le tue categorie elementi");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(R.layout.activity_manage_category_element);
    }

    @Override
    public void updateUI(int layout){

        super.updateUI(layout);

        linearLayout = findViewById(R.id.categoryElementLayout);
        linearLayout.removeAllViews();

        new Thread(() -> {
            List<CategoryAndProduct> categories = IsiAppActivity.isiCashierRequest.getCategories();

            runOnUiThread(() -> {
                if(categories != null){
                    for (CategoryAndProduct categories1 : categories){

                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        final View inflate = inflater.inflate(R.layout.category_table, linearLayout, false);
                        TextView loadinf = inflate.findViewById(R.id.categoryTableText);
                        loadinf.setText(categories1.category.name);

                        CheckBox active = inflate.findViewById(R.id.checkbox_active);
                        CheckBox guest = inflate.findViewById(R.id.chekcbox_guest_active);

                        active.setChecked(categories1.category.active == 1);
                        guest.setChecked(categories1.category.guest == 1);

                        active.setOnCheckedChangeListener((compoundButton, b) -> {
                            categories1.category.active = b ? 1 : 0;

                            new Thread(() -> IsiAppActivity.isiCashierRequest.editcategory(categories1.category)).start();


                        });

                        guest.setOnCheckedChangeListener((compoundButton, b) -> {
                            categories1.category.guest = b ? 1 : 0;

                            new Thread(() -> IsiAppActivity.isiCashierRequest.editcategory(categories1.category)).start();

                        });

                        Button edit = inflate.findViewById(R.id.editCategoryButton);
                        edit.setOnClickListener(v -> {
                            Intent i = new Intent(ManageCategoryElementActivity.this, AddCategoryElementActivity.class);
                            i.putExtra("category", new Gson().toJson(categories1.category));
                            startActivity(i);
                        });


                        linearLayout.addView(inflate);

                    }
                }else{
                    errorPage(layout);
                }
            });

        }).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.intestazione_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addIntestazioneMenu) {
            Intent addCategory = new Intent(ManageCategoryElementActivity.this, AddCategoryElementActivity.class);
            startActivity(addCategory);
        }

        super.onOptionsItemSelected(item);

        return true;
    }
}
