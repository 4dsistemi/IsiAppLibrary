package com.isi.isilibrary.products

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.dialog.NetConnection

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

        NetConnection(
                this,
                "Scarico categorie prodotti...",
                startNetConnection = {
                    IsiAppActivity.httpRequest?.categories
                },
                onConnectionOk = {
                    it!!.categories.sortBy { it1 -> it1.name?.lowercase() }
                    for (categories1 in it.categories) {
                        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val inflate = inflater.inflate(R.layout.category_table, linearLayout, false)

                        val loadinf = inflate.findViewById<TextView>(R.id.categoryTableText)
                        loadinf.text = categories1.name

                        val active = inflate.findViewById<CheckBox>(R.id.checkbox_active)
                        val guest = inflate.findViewById<CheckBox>(R.id.chekcbox_guest_active)

                        active.isChecked = categories1.active == 1
                        guest.isChecked = categories1.guest == 1

                        active.setOnCheckedChangeListener { _: CompoundButton?, b: Boolean ->
                            categories1.active = if (b) 1 else 0

                            Thread {
                                IsiAppActivity.httpRequest!!.editcategory(
                                        categories1
                                )
                            }
                                    .start()
                        }
                        guest.setOnCheckedChangeListener { _: CompoundButton?, b: Boolean ->
                            categories1.guest = if (b) 1 else 0

                            Thread {
                                IsiAppActivity.httpRequest!!.editcategory(
                                        categories1
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
                            i.putExtra("category", Gson().toJson(categories1))
                            startActivity(i)
                        }

                        linearLayout.addView(inflate)
                    }
                },
                onConnectionError = {
                    errorPage(layout)
                }
        ).start()
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