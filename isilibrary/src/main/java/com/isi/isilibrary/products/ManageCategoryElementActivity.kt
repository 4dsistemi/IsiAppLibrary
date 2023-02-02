package com.isi.isilibrary.products

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.gson.Gson
import com.isi.isiapi.classes.CategoryAndProduct
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity

class ManageCategoryElementActivity : BackActivity() {
    private lateinit var linearLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_category_element)
        title = "Le tue categorie elementi"
    }

    override fun onResume() {
        super.onResume()
        updateUI(R.layout.activity_manage_category_element)
    }

    override fun updateUI(layout: Int) {
        super.updateUI(layout)
        linearLayout = findViewById(R.id.categoryElementLayout)
        linearLayout.removeAllViews()
        Thread {
            val categories: MutableList<CategoryAndProduct>? =
                IsiAppActivity.isiCashierRequest?.categories
            runOnUiThread {
                if (categories != null) {
                    categories.sortBy { it.category.name.lowercase() }
                    for (categories1 in categories) {
                        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val inflate = inflater.inflate(R.layout.category_table, linearLayout, false)
                        val loadinf = inflate.findViewById<TextView>(R.id.categoryTableText)
                        loadinf.text = categories1.category.name
                        val active = inflate.findViewById<CheckBox>(R.id.checkbox_active)
                        val guest = inflate.findViewById<CheckBox>(R.id.chekcbox_guest_active)
                        active.isChecked = categories1.category.active == 1
                        guest.isChecked = categories1.category.guest == 1
                        active.setOnCheckedChangeListener { _: CompoundButton?, b: Boolean ->
                            categories1.category.active = if (b) 1 else 0
                            Thread {
                                IsiAppActivity.isiCashierRequest!!.editcategory(
                                    categories1.category
                                )
                            }
                                .start()
                        }
                        guest.setOnCheckedChangeListener { _: CompoundButton?, b: Boolean ->
                            categories1.category.guest = if (b) 1 else 0
                            Thread {
                                IsiAppActivity.isiCashierRequest!!.editcategory(
                                    categories1.category
                                )
                            }
                                .start()
                        }
                        val edit = inflate.findViewById<Button>(R.id.editCategoryButton)
                        edit.setOnClickListener {
                            val i = Intent(
                                this@ManageCategoryElementActivity,
                                AddCategoryElementActivity::class.java
                            )
                            i.putExtra("category", Gson().toJson(categories1.category))
                            startActivity(i)
                        }
                        linearLayout.addView(inflate)
                    }
                } else {
                    errorPage(layout)
                }
            }
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.intestazione_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addIntestazioneMenu) {
            val addCategory =
                Intent(this@ManageCategoryElementActivity, AddCategoryElementActivity::class.java)
            startActivity(addCategory)
        }
        super.onOptionsItemSelected(item)
        return true
    }
}