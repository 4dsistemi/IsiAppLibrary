package com.isi.isilibrary.products

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isi.isiapi.classes.*
import com.isi.isiapi.classes.isiorder.CategoryAndListini
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.dialog.NetConnection
import com.isi.isilibrary.products.recycler.ElementRecycler
import kotlin.collections.ArrayList

class ManageElementsActivity : BackActivity() {
    private lateinit var categorySelected: Category
    private lateinit var layout: RecyclerView
    private lateinit var recycler: ElementRecycler
    private var products: MutableList<Product> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_elements)
        title = "Gestisci elementi"

        val cat = Category(0, "Tutto", 0, "", 0)
        categorySelected = cat

        layout = findViewById(R.id.product_list_recycler)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        layout.layoutManager = linearLayoutManager
    }

    override fun onResume() {
        super.onResume()

        NetConnection<CategoryAndListini>(this,
                "Scarico elementi...",
                startNetConnection = {
                    IsiAppActivity.httpRequest!!.categories
                },
                onConnectionOk = {
                    products.clear()

                    for (categoryAndProduct in it.categories) {
                        products.addAll(categoryAndProduct.products!!)
                    }

                    products.sortBy { it2 -> it2.name.lowercase() }

                    recycler = ElementRecycler(this, products)
                    layout.adapter = recycler
                    recycler.search(categorySelected.id, "")
                    val search = findViewById<SearchView>(R.id.searchElement)
                    search.isClickable = true
                    search.queryHint = "Cerca"
                    search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(s: String): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(s: String): Boolean {
                            recycler.search(categorySelected.id, s)
                            return true
                        }
                    })
                },
                onConnectionError = {

                }
        ).start()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.manage_element_menu, menu)
        val item = menu.findItem(R.id.manage_element_spinner)
        val spinner = item.actionView as Spinner?
        Thread {
            val categories: CategoryAndListini? =
                    IsiAppActivity.httpRequest!!.categories

            if (categories != null) {
                val cat = Category(0, "Tutto", 0, "", 0)
                categories.categories.add(0, cat)
                categorySelected = cat
                runOnUiThread {
                    val adapter = ArrayAdapter(
                            this@ManageElementsActivity,
                            android.R.layout.simple_spinner_item,
                            categories.categories
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner!!.adapter = adapter
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View,
                                position: Int,
                                id: Long
                        ) {
                            if (categories.categories[position] !== categorySelected) {
                                categorySelected = categories.categories[position]
                                recycler.search(categorySelected.id, "")
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
            }

        }.start()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addIntestazioneMenu) {
            val intent = Intent(this@ManageElementsActivity, AddManageElementActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}