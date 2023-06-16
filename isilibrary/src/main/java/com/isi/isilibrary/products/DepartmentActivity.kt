package com.isi.isilibrary.products

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.isi.isiapi.classes.isicash.IsiCashDepartment
import com.isi.isiapi.classes.isiorder.CategoryAndListini
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.dialog.Dialog
import java.util.Locale

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

            val rates: MutableList<IsiCashDepartment>? =
                IsiAppActivity.httpRequest!!.department

            val products: CategoryAndListini? =
                IsiAppActivity.httpRequest!!.categories

            if (rates == null || products == null) {
                runOnUiThread { Dialog(this).showErrorConnectionDialog(true) }
            } else {
                rates.sortBy { it.department }
                for (rate in rates) {
                    val inflater = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                    val inflate = inflater.inflate(R.layout.department_cell, departmentLayout, false)
                    val name = inflate.findViewById<TextView>(R.id.nameDepartmentCellText)
                    name.text = String.format(
                        Locale.getDefault(),
                        "Reparto %d a %s",
                        rate.department,
                        Rates.getRatesValor(rate.code)
                    )
                    val description = inflate.findViewById<TextView>(R.id.descriptionRateTextCell)
                    if (rate.product_id != null) {
                        for (categoryAndProduct in products.categories) {
                            for (prod in categoryAndProduct.products!!) {
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