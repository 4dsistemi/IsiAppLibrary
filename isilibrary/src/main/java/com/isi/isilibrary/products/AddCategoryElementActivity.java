package com.isi.isilibrary.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.isi.isiapi.classes.Category;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;

public class AddCategoryElementActivity extends BackActivity {

    private Category back;
    private EditText category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_element);

        setTitle("Aggiungi categoria");

        category = findViewById(R.id.categoryElementEdit);

        Intent backIntent = getIntent();

        if(backIntent.getStringExtra("category") != null){
            back = new Gson().fromJson(backIntent.getStringExtra("category"), Category.class);
            setTitle("Modifica categoria");
            category.setText(back.name);
        }

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
            if(category.getText().toString().trim().equals("")){

                Toast.makeText(this, "Il nome non può essere vuoto", Toast.LENGTH_LONG).show();

            }else{

                if(back != null){
                    back.image = "default.png";
                    back.name = category.getText().toString().trim();
                    new Thread(() -> {
                        if(IsiAppActivity.isiCashierRequest.editcategory(back)){
                            runOnUiThread(this::finish);
                        }
                    }).start();

                }else{
                    new Thread(() -> {
                        if(IsiAppActivity.isiCashierRequest.addCategory(new Category(0, category.getText().toString().trim(),0,  "default.png"))){
                            runOnUiThread(this::finish);
                        }
                    }).start();
                }

            }


        }

        return super.onOptionsItemSelected(item);
    }

}
