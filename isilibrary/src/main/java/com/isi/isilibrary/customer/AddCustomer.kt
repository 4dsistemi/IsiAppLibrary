package com.isi.isilibrary.customer

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.gson.Gson
import com.isi.isiapi.classes.Customer
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.cfbuilder.CF_Builder
import com.isi.isilibrary.cfbuilder.PersonalData
import com.isi.isilibrary.dialog.Dialog
import java.util.*

class AddCustomer : BackActivity() {
    private var aeCode: EditText? = null
    private var ivaEditCustomer: EditText? = null
    private var addressAddCustomer: EditText? = null
    private var cityAddCustomer: EditText? = null
    private var capAddCustomer: EditText? = null
    private var provinceAddCustomer: EditText? = null
    private var countryAddCustomer: EditText? = null
    private var pecAddCustomer: EditText? = null
    private var phoneAddCustomer: EditText? = null
    private var societyAddCustomer: EditText? = null
    private var fiscalAddCustomer: EditText? = null
    private var nameAddCustomer: EditText? = null
    private var surnameAddCustomer: EditText? = null
    private var emailAddCustomer: EditText? = null
    private var backCustomer: Customer? = null
    private var birthplaceCustomer: EditText? = null
    private var birthdayCustomer: EditText? = null
    private var privacyAccepted: CheckBox? = null
    private var commercialAccepted: CheckBox? = null
    private var male: RadioButton? = null
    private var female: RadioButton? = null
    private fun findViews() {
        aeCode = findViewById(R.id.aeCode)
        ivaEditCustomer = findViewById(R.id.ivaEditCustomer)
        addressAddCustomer = findViewById(R.id.addressAddCustomer)
        cityAddCustomer = findViewById(R.id.cityAddCustomer)
        capAddCustomer = findViewById(R.id.capAddCustomer)
        provinceAddCustomer = findViewById(R.id.provinceAddCustomer)
        countryAddCustomer = findViewById(R.id.countryAddCustomer)
        pecAddCustomer = findViewById(R.id.pecAddCustomer)
        phoneAddCustomer = findViewById(R.id.phoneAddCustomer)
        societyAddCustomer = findViewById(R.id.societyAddCustomer)
        fiscalAddCustomer = findViewById(R.id.fiscalAddCustomer)
        nameAddCustomer = findViewById(R.id.nameAddCustomer)
        surnameAddCustomer = findViewById(R.id.surnameAddCustomer)
        emailAddCustomer = findViewById(R.id.emailAddCustomer)
        birthplaceCustomer = findViewById(R.id.birthplaceCustomer)
        birthdayCustomer = findViewById(R.id.birthdayAddCustomer)
        privacyAccepted = findViewById(R.id.privacyAccepted)
        commercialAccepted = findViewById(R.id.commercialAccepted)
        male = findViewById(R.id.maleAddCustomer)
        female = findViewById(R.id.femaleAddCustomer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)
        title = "Gestisci clienti"
        findViews()
        val back = intent
        if (back.getStringExtra("customer") != null) {
            backCustomer = Gson().fromJson(back.getStringExtra("customer"), Customer::class.java)
        }
        if (backCustomer != null) {
            setTextViewtext(aeCode, backCustomer!!.aeCode)
            setTextViewtext(ivaEditCustomer, backCustomer!!.iva)
            setTextViewtext(addressAddCustomer, backCustomer!!.address)
            setTextViewtext(cityAddCustomer, backCustomer!!.city)
            setTextViewtext(capAddCustomer, backCustomer!!.zip)
            setTextViewtext(provinceAddCustomer, backCustomer!!.province)
            setTextViewtext(countryAddCustomer, backCustomer!!.country)
            setTextViewtext(pecAddCustomer, backCustomer!!.pec)
            setTextViewtext(phoneAddCustomer, backCustomer!!.phone)
            setTextViewtext(societyAddCustomer, backCustomer!!.society)
            setTextViewtext(fiscalAddCustomer, backCustomer!!.fiscal)
            setTextViewtext(nameAddCustomer, backCustomer!!.name)
            setTextViewtext(surnameAddCustomer, backCustomer!!.surname)
            setTextViewtext(emailAddCustomer, backCustomer!!.email)
            setTextViewtext(birthplaceCustomer, backCustomer!!.birthplace)
            setTextViewtext(birthdayCustomer, backCustomer!!.birthday)
            if (backCustomer!!.commercialComunication) {
                commercialAccepted!!.isChecked = true
            }
            if (backCustomer!!.gender == 1) {
                female!!.isChecked = true
            }
            privacyAccepted!!.isChecked = true
        }
        val generateFiscalCode = findViewById<Button>(R.id.generateFiscalCode)
        generateFiscalCode.setOnClickListener {
            val pDialog = Dialog(this).showLoadingDialog("Genero codice fiscale...")
            pDialog.dismiss()
            try {
                val birthday = birthdayCustomer!!.text.toString()
                val info = birthday.split("-").toTypedArray()
                val day = info[0]
                val month = info[1]
                val year = info[2]
                val p = PersonalData(
                    upperFirstLetter(nameAddCustomer!!.text.toString()),  // String ("Mario")
                    upperFirstLetter(surnameAddCustomer!!.text.toString()),  // String ("Rossi")
                    day,  // String ("16")
                    month,  // String ("03")
                    year,  // String ("1985")
                    male!!.isChecked,  // boolean value  (Man = true, Female = false)
                    upperFirstLetter(birthplaceCustomer!!.text.toString()) // String ("Milan")
                )
                CF_Builder.init(applicationContext)
                val codice_fiscale = CF_Builder.build(p)
                fiscalAddCustomer!!.setText(codice_fiscale)
            } catch (e: Exception) {
                Dialog(this).showCustomErrorConnectionDialog("Non è stato possibile calcolare il codice fiscale")
            }
        }
    }

    private fun upperFirstLetter(toUpper: String): String {
        return toUpper.substring(0, 1).uppercase(Locale.getDefault()) + toUpper.substring(1)
            .lowercase(
                Locale.getDefault()
            )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.add_intestazione_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.addIntestazioneDone) {
            if (!privacyAccepted!!.isChecked) {
                Dialog(this).showCustomErrorConnectionDialog("Non si può aggiungere un cliente se non ha accettato e letto le norme sulla privacy")
            } else if (nameAddCustomer!!.text.toString()
                    .trim { it <= ' ' } == "" || surnameAddCustomer!!.text.toString()
                    .trim { it <= ' ' } == ""
            ) {
                Dialog(this).showCustomErrorConnectionDialog("Nome e cognome non possono essere vuoti")
            } else {
                val c = Customer(
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
                    commercialAccepted!!.isChecked
                )
                c.birthplace = getTextNullTextView(birthplaceCustomer)
                if (male!!.isChecked) {
                    c.gender = 0
                } else {
                    c.gender = 1
                }
                Thread {
                    val ok: Boolean
                    if (backCustomer != null) {
                        c.id = backCustomer!!.id
                        ok = IsiAppActivity.isiCashierRequest!!.editCustomer(c)
                    } else {
                        ok = IsiAppActivity.isiCashierRequest!!.addCustomer(c)
                    }
                    runOnUiThread {
                        if (ok) {
                            finish()
                        } else {
                            Dialog(this).showErrorConnectionDialog(false)
                        }
                    }
                }.start()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTextNullTextView(textView: TextView?): String {
        return if (textView!!.text.toString() == "") {
            ""
        } else textView.text.toString()
    }

    private fun setTextViewtext(textView: EditText?, added: String?) {
        if (added == null) {
            textView!!.setText("")
            return
        }
        textView!!.setText(added)
    }
}