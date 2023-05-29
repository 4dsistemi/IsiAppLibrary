package com.isi.isilibrary.products

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.isi.isiapi.classes.CategoryAndProduct
import com.isi.isiapi.classes.Product
import com.isi.isiapi.classes.isicash.IsiCashDepartment
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.dialog.Dialog
import com.isi.isilibrary.dialog.NetConnection
import java.util.*
import kotlin.collections.ArrayList

class AddDepartmentsActivity : BackActivity() {
    private var productId: Int? = null
    private var backDepartment: IsiCashDepartment? = null
    private lateinit var code: TextInputEditText
    private lateinit var spinnerRate: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_departments)
        title = "Reparti"
    }

    override fun onResume() {
        super.onResume()
        val arrayRate = resources.getStringArray(R.array.rate_percents)
        val spinner = findViewById<AutoCompleteTextView>(R.id.productSpinnerDepartment)
        code = findViewById(R.id.departmentCodeEdit)
        spinnerRate = findViewById(R.id.departmentSpinnerCode)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayRate)
        spinnerRate.setAdapter(adapter2)
        spinnerRate.setText("A - 4%", false)
        val names: MutableList<String> = ArrayList()
        names.add("Default")
        val pDialog = Dialog(this).showLoadingDialog("Aggiorno reparti...")

        Thread {
            val rates: MutableList<IsiCashDepartment>? =
                IsiAppActivity.httpRequest!!.department
            val products = ArrayList<Product>()
            val categoryAndProducts: MutableList<CategoryAndProduct>? =
                IsiAppActivity.httpRequest!!.categories

            if (categoryAndProducts != null)
                for (cat in categoryAndProducts) {
                    if(cat.product != null){
                        products.addAll(cat.product!!)
                    }
                }
            runOnUiThread {
                pDialog.dismiss()
                if(rates != null){
                    rates.sortWith(Comparator.comparingInt { departments: IsiCashDepartment -> departments.department })
                    for (p in products) {
                        names.add(p.name)
                    }
                    val adapter = ArrayAdapter(
                        this@AddDepartmentsActivity,
                        android.R.layout.simple_dropdown_item_1line, names
                    )
                    spinner.setAdapter(adapter)
                    spinner.onItemClickListener =
                        OnItemClickListener { _: AdapterView<*>?, _: View?, i: Int, _: Long ->
                            Log.e("TAG", "onResume: ")
                            productId = if (i > 0) {
                                products[i - 1].id
                            } else {
                                null
                            }
                        }
                    spinner.setText(names[0], false)
                    if (intent.getBooleanExtra("modify", false)) {
                        for (departments in rates) {
                            if (departments.id == intent.getIntExtra("id", -1)) {
                                code.setText(
                                    String.format(
                                        Locale.getDefault(),
                                        "%d",
                                        departments.department
                                    )
                                )
                                backDepartment = departments
                                for (s in arrayRate) {
                                    if (s.contains(departments.code + " -")) {
                                        spinnerRate.setText(s, false)
                                    }
                                }
                                if (departments.product_id != null) {
                                    for (p in products) {
                                        if (p.id == departments.product_id) {
                                            spinner.setText(p.name)
                                            productId = p.id
                                        }
                                    }
                                }
                            }
                        }
                }

                }else{
                    Dialog(this).showErrorConnectionDialog(true)
                }
            }
        }.start()
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
            if (backDepartment != null) {
                Thread {
                    backDepartment!!.department = code.text.toString().toInt()
                    backDepartment!!.code =
                        spinnerRate.text.toString().split(" - ").toTypedArray()[0]
                    backDepartment!!.product_id = productId
                    val result: Boolean =
                        IsiAppActivity.httpRequest!!.editDepartment(backDepartment)
                    runOnUiThread {
                        if (result) {
                            finish()
                            Toast.makeText(
                                this@AddDepartmentsActivity, "Reparto modificato correttamente!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Dialog(this).showErrorConnectionDialog(false)
                        }
                    }
                }.start()
            } else {
                Thread {
                    val department = IsiCashDepartment(
                        0,
                        code.text.toString().toInt(),
                        productId,
                        spinnerRate.text.toString().split(" - ").toTypedArray()[0]
                    )
                    val result: Boolean =
                        IsiAppActivity.httpRequest!!.addDepartment(department)
                    runOnUiThread {
                        if (result) {
                            finish()
                            Toast.makeText(
                                this@AddDepartmentsActivity, "Reparto aggiunto correttamente!",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Dialog(this).showErrorConnectionDialog(false)
                        }
                    }
                }.start()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}