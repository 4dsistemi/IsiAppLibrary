package com.isi.isilibrary.customer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.backActivity.BackActivity;
import com.isi.isilibrary.cfbuilder.CF_Builder;
import com.isi.isilibrary.cfbuilder.PersonalData;
import com.isi.isiapi.classes.Customer;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddCustomer extends BackActivity {

    private EditText aeCode;
    private EditText ivaEditCustomer;
    private EditText addressAddCustomer;
    private EditText cityAddCustomer;
    private EditText capAddCustomer;
    private EditText provinceAddCustomer;
    private EditText countryAddCustomer;
    private EditText pecAddCustomer;
    private EditText phoneAddCustomer;
    private EditText societyAddCustomer;
    private EditText fiscalAddCustomer;
    private EditText nameAddCustomer;
    private EditText surnameAddCustomer;
    private EditText emailAddCustomer;
    private Customer backCustomer;
    private EditText birthplaceCustomer;
    private EditText birthdayCustomer;
    private CheckBox privacyAccepted;
    private CheckBox commercialAccepted;
    private RadioButton male;
    private RadioButton female;

    private void findViews() {
        aeCode = findViewById( R.id.aeCode );
        ivaEditCustomer = findViewById( R.id.ivaEditCustomer );
        addressAddCustomer = findViewById( R.id.addressAddCustomer );
        cityAddCustomer = findViewById( R.id.cityAddCustomer );
        capAddCustomer = findViewById( R.id.capAddCustomer );
        provinceAddCustomer = findViewById( R.id.provinceAddCustomer );
        countryAddCustomer = findViewById( R.id.countryAddCustomer );
        pecAddCustomer = findViewById( R.id.pecAddCustomer );
        phoneAddCustomer = findViewById( R.id.phoneAddCustomer );
        societyAddCustomer = findViewById( R.id.societyAddCustomer );
        fiscalAddCustomer = findViewById( R.id.fiscalAddCustomer );
        nameAddCustomer = findViewById( R.id.nameAddCustomer );
        surnameAddCustomer = findViewById( R.id.surnameAddCustomer );
        emailAddCustomer = findViewById( R.id.emailAddCustomer );
        birthplaceCustomer = findViewById( R.id.birthplaceCustomer );
        birthdayCustomer = findViewById(R.id.birthdayAddCustomer);
        privacyAccepted = findViewById(R.id.privacyAccepted);
        commercialAccepted = findViewById(R.id.commercialAccepted);
        male = findViewById(R.id.maleAddCustomer);
        female = findViewById(R.id.femaleAddCustomer);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        setTitle("Gestisci clienti");

        findViews();

        Intent back = getIntent();

        if(back.getStringExtra("customer") != null){
            backCustomer = new Gson().fromJson(back.getStringExtra("customer"), Customer.class);
        }

        if(backCustomer != null){
            setTextViewtext(aeCode, backCustomer.aeCode);
            setTextViewtext(ivaEditCustomer, backCustomer.iva);
            setTextViewtext(addressAddCustomer, backCustomer.address);
            setTextViewtext(cityAddCustomer, backCustomer.city);
            setTextViewtext(capAddCustomer, backCustomer.zip);
            setTextViewtext(provinceAddCustomer, backCustomer.province);
            setTextViewtext(countryAddCustomer, backCustomer.country);
            setTextViewtext(pecAddCustomer, backCustomer.pec);
            setTextViewtext(phoneAddCustomer, backCustomer.phone);
            setTextViewtext(societyAddCustomer, backCustomer.society);
            setTextViewtext(fiscalAddCustomer, backCustomer.fiscal);
            setTextViewtext(nameAddCustomer, backCustomer.name);
            setTextViewtext(surnameAddCustomer, backCustomer.surname);
            setTextViewtext(emailAddCustomer, backCustomer.email);
            setTextViewtext(birthplaceCustomer, backCustomer.birthplace);
            setTextViewtext(birthdayCustomer, backCustomer.birthday);
            if(backCustomer.commercialComunication){
                commercialAccepted.setChecked(true);
            }

            if(backCustomer.gender == 1){
                female.setChecked(true);
            }
            privacyAccepted.setChecked(true);

        }

        Button generateFiscalCode = findViewById(R.id.generateFiscalCode);

        generateFiscalCode.setOnClickListener(v -> {

            SweetAlertDialog pDialog = new SweetAlertDialog(AddCustomer.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Genero codice fiscale...");
            pDialog.setCancelable(false);
            pDialog.show();

            pDialog.dismissWithAnimation();

            try{
                String birthday = birthdayCustomer.getText().toString();
                String[] info = birthday.split("-");
                String day = info[0];
                String month = info[1];
                String year = info[2];

                PersonalData p = new PersonalData(
                        upperFirstLetter(nameAddCustomer.getText().toString()),       // String ("Mario")
                        upperFirstLetter(surnameAddCustomer.getText().toString()),    // String ("Rossi")
                        day,         // String ("16")
                        month,         // String ("03")
                        year,       // String ("1985")
                        male.isChecked(),        // boolean value  (Man = true, Female = false)
                        upperFirstLetter(birthplaceCustomer.getText().toString())  // String ("Milan")
                );

                CF_Builder.init(getApplicationContext());

                String codice_fiscale = CF_Builder.build(p);

                fiscalAddCustomer.setText(codice_fiscale);
            }catch (Exception e){
                new SweetAlertDialog(AddCustomer.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Attenzione")
                        .setContentText("Non è stato possibile calcolare il codice fiscale")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation).show();
            }


        });

    }

    private String upperFirstLetter(String toUpper){
        return toUpper.substring(0, 1).toUpperCase() + toUpper.substring(1).toLowerCase();
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

            if(!privacyAccepted.isChecked()){

                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Attenzione")
                        .setContentText("Non si può aggiungere un cliente se non ha accettato e letto le norme sulla privacy")
                        .setConfirmText("OK!")
                        .show();


            }else if(nameAddCustomer.getText().toString().trim().equals("") || surnameAddCustomer.getText().toString().trim().equals("")){

                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Attenzione")
                        .setContentText("Nome e cognome non possono essere vuoti")
                        .setConfirmText("OK!")
                        .show();

            }else{

                final Customer c = new Customer(
                        getTextNullTextView(nameAddCustomer),
                        getTextNullTextView(surnameAddCustomer),
                        getTextNullTextView(ivaEditCustomer),
                        getTextNullTextView(emailAddCustomer),
                        getTextNullTextView(addressAddCustomer),
                        getTextNullTextView(cityAddCustomer),
                        getTextNullTextView(provinceAddCustomer),
                        getTextNullTextView(capAddCustomer),
                        getTextNullTextView(countryAddCustomer),
                        getTextNullTextView(phoneAddCustomer),
                        getTextNullTextView(pecAddCustomer),
                        getTextNullTextView(aeCode),
                        getTextNullTextView(birthdayCustomer),
                        getTextNullTextView(societyAddCustomer),
                        getTextNullTextView(fiscalAddCustomer),
                        commercialAccepted.isChecked());

                c.birthplace = getTextNullTextView(birthplaceCustomer);
                if(male.isChecked()){
                    c.gender = 0;
                }else{
                    c.gender = 1;
                }

                new Thread(() -> {

                    boolean ok;

                    if(backCustomer != null){
                        c.id = backCustomer.id;
                        ok = IsiAppActivity.isiCashierRequest.editCustomer(c);

                    }else{
                        ok = IsiAppActivity.isiCashierRequest.addCustomer(c);

                    }

                    runOnUiThread(() -> {
                        if(ok){
                            finish();
                        }else{
                            new SweetAlertDialog(AddCustomer.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Attenzione")
                                    .setContentText("Problema di connessione, riprovare")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation)
                                    .show();
                        }
                    });


                }).start();

            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getTextNullTextView(TextView textView){
        if(textView.getText().toString().equals("")){
            return null;
        }

        return textView.getText().toString();
    }

    private void setTextViewtext(EditText textView, String added){
        if(added == null){
            textView.setText("");
            return;
        }

        textView.setText(added);
    }

}
