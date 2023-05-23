package com.isi.isilibrary.products.ingredients

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.isi.isiapi.classes.Ingredients
import com.isi.isiapi.classes.isimaga.ProductForniture
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity

class AddIngredientsActivity : BackActivity() {
    private lateinit var ingredientsLayout: RecyclerView
    private var ingredientsAdd: Ingredients? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredients)
        title = "Aggiungi ingredienti"
        updateUI(R.layout.activity_add_ingredients)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val resultIntent = Intent()

            if(ingredientsAdd == null){
                setResult(RESULT_CANCELED, resultIntent)
                finish()
            }else{
                val gson = Gson()
                resultIntent.putExtra("ingredient", gson.toJson(ingredientsAdd))
                setResult(RESULT_OK, resultIntent)
                finish()
            }

        }
    }

    override fun updateUI(layout: Int) {
        super.updateUI(layout)

        Thread {
            val storage: List<ProductForniture>? =
                IsiAppActivity.httpRequest!!.isimagaGetProductForniture()
            if (storage == null) {
                errorPage(layout)
            } else {
                runOnUiThread {
                    storage.sortedBy { productForniture -> productForniture.name.lowercase() }

                    ingredientsLayout = findViewById(R.id.ingredientRecycler)

                    val linearLayoutManager = LinearLayoutManager(this)
                    linearLayoutManager.orientation = RecyclerView.VERTICAL
                    ingredientsLayout.layoutManager = linearLayoutManager

                    val ingredientsRecycler = IngredientsRecycler(
                        context = this,
                        product = storage,
                    ) {
                        ingredientsAdd = it
                        onBackPressedDispatcher.onBackPressed()
                    }

                    ingredientsLayout.adapter = ingredientsRecycler

                    val searchView = findViewById<SearchView>(R.id.searchIngredients)
                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(s: String): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(s: String): Boolean {
                            ingredientsRecycler.search(s)
                            return true
                        }
                    })
                }
            }
        }.start()
    }
}