package com.isi.isilibrary.products

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isi.isiapi.classes.*
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.products.recycler.ElementRecycler
import kotlin.collections.ArrayList

class ManageElementsActivity : BackActivity() {
    private var categorySelected: CategoryAndProduct? = null
    private lateinit var layout: RecyclerView
    private var products: MutableList<Product> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_elements)
        title = "Gestisci elementi"
        layout = findViewById(R.id.product_list_recycler)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        layout.layoutManager = linearLayoutManager
    }

    override fun onResume() {
        super.onResume()
        Thread {
            products.clear()
            val categoryAndProducts: List<CategoryAndProduct> =
                IsiAppActivity.isiCashierRequest!!.categories
            for (categoryAndProduct in categoryAndProducts) {
                products.addAll(categoryAndProduct.product)
            }
            products.sortBy { it.name.lowercase() }
            runOnUiThread {
                updateUi("")
                val search = findViewById<SearchView>(R.id.searchElement)
                search.isClickable = true
                search.queryHint = "Cerca"
                search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(s: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(s: String): Boolean {
                        runOnUiThread { updateUi(s) }
                        return true
                    }
                })
            }
        }.start()
    }

    private fun updateUi(s: String) {
        val recycler: ElementRecycler
        if (categorySelected != null) {
            recycler = if (categorySelected!!.category.id == 0) {
                ElementRecycler(this, products.filter { it.name.lowercase().contains(s.lowercase() )})
            } else {
                ElementRecycler(this, products.filter {
                        it.name.lowercase().contains(
                            s.lowercase()
                        ) && it.category_id == categorySelected!!.category.id
                    })
            }
            layout.adapter = recycler
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.manage_element_menu, menu)
        val item = menu.findItem(R.id.manage_element_spinner)
        val spinner = item.actionView as Spinner?
        Thread {
            val categories: MutableList<CategoryAndProduct>? =
                IsiAppActivity.isiCashierRequest!!.categories

            if(categories != null){
                val cat = CategoryAndProduct()
                cat.category = Category(0, "Tutto", 0, "")
                categories.add(0, cat)
                runOnUiThread {
                    val adapter = ArrayAdapter(
                        this@ManageElementsActivity,
                        android.R.layout.simple_spinner_item,
                        categories
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
                            if (categories[position] !== categorySelected) {
                                categorySelected = categories[position]
                                updateUi("")
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