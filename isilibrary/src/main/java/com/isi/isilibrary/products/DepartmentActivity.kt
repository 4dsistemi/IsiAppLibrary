package com.isi.isilibrary.products

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import com.isi.isiapi.classes.CategoryAndProduct
import com.isi.isiapi.classes.isicash.IsiCashDepartment
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.dialog.Dialog
import java.util.*

class DepartmentActivity : BackActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department)
        title = "I tuoi reparti"
    }

    override fun onResume() {
        super.onResume()
        val departmentLayout = findViewById<LinearLayout>(R.id.departmentLayout)
        departmentLayout.removeAllViews()
        val pDialog = Dialog(this).showLoadingDialog("Aggiorno reparti...")
        Thread {
            var rates: List<IsiCashDepartment>? =
                IsiAppActivity.isiCashierRequest!!.department
            val products: List<CategoryAndProduct>? =
                IsiAppActivity.isiCashierRequest!!.categories
            if (rates == null || products == null) {
                runOnUiThread { Dialog(this).showErrorConnectionDialog(true) }
            } else {
                rates = rates.sortedWith(Comparator.comparingInt { departments: IsiCashDepartment -> departments.department })
                for (rate in rates) {
                    val inflater = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                    @SuppressLint("InflateParams") val inflate =
                        inflater.inflate(R.layout.department_cell, departmentLayout, false)
                    val name = inflate.findViewById<TextView>(R.id.nameDepartmentCellText)
                    name.text = String.format(
                        Locale.getDefault(),
                        "Reparto %d a %s",
                        rate.department,
                        Rates.getRatesValor(rate.code)
                    )
                    val description = inflate.findViewById<TextView>(R.id.descriptionRateTextCell)
                    if (rate.product_id != null) {
                        for (categoryAndProduct in products) {
                            for (prod in categoryAndProduct.product) {
                                if (prod.id == rate.product_id) {
                                    description.text = String.format("Descrizione %s", prod.name)
                                }
                            }
                        }
                    }
                    val modify = inflate.findViewById<Button>(R.id.modifyDepartmentButton)
                    modify.setOnClickListener {
                        runOnUiThread {
                            val i =
                                Intent(this@DepartmentActivity, AddDepartmentsActivity::class.java)
                            i.putExtra("modify", true)
                            i.putExtra("id", rate.id)
                            startActivity(i)
                        }
                    }
                    runOnUiThread { departmentLayout.addView(inflate) }
                }
                runOnUiThread { pDialog.dismiss() }
            }
        }.start()
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
            val i = Intent(this@DepartmentActivity, AddDepartmentsActivity::class.java)
            startActivity(i)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}