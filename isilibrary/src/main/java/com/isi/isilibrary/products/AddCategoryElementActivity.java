package com.isi.isilibrary.products;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.isi.isiapi.classes.Category;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddCategoryElementActivity extends BackActivity {

    private Category back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_element);

        setTitle("Aggiungi categoria");

        Button addCategory = findViewById(R.id.addCategoryElement);
        EditText category = findViewById(R.id.categoryElementEdit);

        Intent backIntent = getIntent();

        if(backIntent.getStringExtra("category") != null){
            back = new Gson().fromJson(backIntent.getStringExtra("category"), Category.class);
            setTitle("Modifica categoria");
            category.setText(back.name);
            addCategory.setText(R.string.modify);
        }

        addCategory.setOnClickListener(view -> {

            if(category.getText().toString().trim().equals("")){

                Toast.makeText(this, "Il nome non può essere vuoto", Toast.LENGTH_LONG).show();

            }else{

                if(back != null){
                    back.image = image;
                    back.name = category.getText().toString().trim();
                    new Thread(() -> {
                        if(IsiAppActivity.isiCashierRequest.editcategory(back)){
                            runOnUiThread(this::finish);
                        }
                    }).start();

                }else{
                    new Thread(() -> {
                        if(IsiAppActivity.isiCashierRequest.addCategory(new Category(0, category.getText().toString().trim(),0,  image))){
                            runOnUiThread(this::finish);
                        }
                    }).start();
                }

            }

        });

        imageView = findViewById(R.id.categoryElementImageView);

    }

    private ImageView imageView;

    private String image = "default.png";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            assert data != null;
            image = data.getStringExtra("image");

            new Thread(() -> {

                Bitmap bitmap = getBitmapFromURL(image);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 600, 200, false));
                    }
                });

            }).start();

        }
    }

    private Bitmap getBitmapFromURL(String src) {

        String urlString = "http://www.ctzon.it/foodImages/categoryImages/" + src;

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
