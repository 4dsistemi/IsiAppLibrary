package com.isi.isilibrary.customer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.isi.isiapi.general.classes.Customer;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;

import java.util.Comparator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyCustomerActivity extends BackActivity {

    private RecyclerView recyclerView;
    private CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mt_customer);

        setTitle("Seleziona Cliente");

        Intent back = getIntent();
        if(back.getStringExtra("customer") != null){
            setTitle("Seleziona Minore");
        }

        recyclerView = findViewById(R.id.customerRecycler);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        layoutManager.setAlignItems(AlignItems.CENTER);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        recyclerView.setLayoutManager(layoutManager);

        SearchView customerSearch = findViewById(R.id.customerSearcView);
        customerSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean searching = getIntent().getBooleanExtra("searching", false);

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Aggiorno Clienti...");
        pDialog.setCancelable(false);
        pDialog.show();

        new Thread(() -> {

            List<Customer> customers = IsiAppActivity.isiCashierRequest.getCustomers(IsiAppActivity.serial);

            runOnUiThread(() -> {
                pDialog.dismissWithAnimation();

                if(customers != null){

                    customers.sort(Comparator.comparing(Customer::getSurname));

                    adapter = new CustomerAdapter(MyCustomerActivity.this, customers, searching);

                    recyclerView.setAdapter(adapter);
                }else{
                    runOnUiThread(() -> new SweetAlertDialog(MyCustomerActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Attenzione")
                            .setContentText("Errore di comunicazione con il server. Riprovare")
                            .setConfirmText("Ok")
                            .setConfirmClickListener(sweetAlertDialog -> {
                                sweetAlertDialog.dismissWithAnimation();
                                finish();
                            }).show());
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

            Intent i = new Intent(MyCustomerActivity.this, AddCustomer.class);
            startActivity(i);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}