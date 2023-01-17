package com.isi.isilibrary.customer

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.isi.isiapi.classes.Customer
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.dialog.Dialog

class MyCustomerActivity : BackActivity() {
    private lateinit var recyclerView: RecyclerView
    private var adapter: CustomerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mt_customer)
        title = "Seleziona Cliente"
        val back = intent
        if (back.getStringExtra("customer") != null) {
            title = "Seleziona Minore"
        }
        recyclerView = findViewById(R.id.customerRecycler)
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.CENTER
        layoutManager.flexWrap = FlexWrap.WRAP
        recyclerView.layoutManager = layoutManager
        val customerSearch = findViewById<SearchView>(R.id.customerSearcView)
        customerSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter!!.filter.filter(newText)
                return true
            }
        })
    }

    fun updateUI() {
        val searching = intent.getBooleanExtra("searching", false)
        val pDialog = Dialog(this).showLoadingDialog("Aggiorno clienti...")
        Thread {
            val customers: MutableList<Customer>? =
                IsiAppActivity.isiCashierRequest!!.customers
            runOnUiThread {
                pDialog.dismiss()
                if (customers != null) {
                    customers.sortWith(Comparator.comparing { customer: Customer -> customer.surname })
                    adapter = CustomerAdapter(this@MyCustomerActivity, customers, searching)
                    recyclerView.adapter = adapter
                } else {
                    runOnUiThread { Dialog(this@MyCustomerActivity).showErrorConnectionDialog(true) }
                }
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.intestazione_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.addIntestazioneMenu) {
            val i = Intent(this@MyCustomerActivity, AddCustomer::class.java)
            startActivity(i)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}