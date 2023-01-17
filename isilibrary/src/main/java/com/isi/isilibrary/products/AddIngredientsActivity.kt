package com.isi.isilibrary.products

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import com.google.gson.Gson
import com.isi.isiapi.classes.Ingredients
import com.isi.isiapi.classes.isimaga.ProductForniture
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.dialog.Dialog
import com.isi.isilibrary.dialog.MaterialTextAndListener
import com.isi.isilibrary.dialog.RapidEditText
import java.util.*

class AddIngredientsActivity : BackActivity() {
    private lateinit var ingredientsLayout: LinearLayout
    private var ingredientsAdd: ArrayList<Ingredients>? = null
    private var search = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredients)
        title = "Aggiungi ingredienti"
        ingredientsAdd = ArrayList()
        updateUI(R.layout.activity_add_ingredients)
    }

    override fun updateUI(layout: Int) {
        super.updateUI(layout)
        ingredientsLayout = findViewById(R.id.addIngredientsLayout)
        val searchView = findViewById<SearchView>(R.id.searchIngredients)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                search = s
                updateUI(layout)
                return true
            }
        })
        val done = findViewById<Button>(R.id.doneAddIngredients)
        done.setOnClickListener { onBackPressed() }
        ingredientsLayout.removeAllViews()
        Thread {
            val storage: List<ProductForniture>? =
                IsiAppActivity.isiCashierRequest!!.isimagaGetProductForniture()
            if (storage == null) {
                errorPage(layout)
            } else {
                for (ingredient in storage) {
                    runOnUiThread {
                        if (ingredient.name.lowercase(Locale.getDefault()).contains(
                                search.lowercase(
                                    Locale.getDefault()
                                )
                            )
                        ) {
                            val inflater =
                                getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            val inflate = inflater.inflate(
                                R.layout.add_ingredients_layout,
                                ingredientsLayout,
                                false
                            )
                            val text = inflate.findViewById<TextView>(R.id.nameElementText)
                            val plus = inflate.findViewById<Button>(R.id.buttonPlusLayout)
                            text.text = ingredient.name
                            ingredientsLayout.addView(inflate)
                            plus.setOnClickListener {
                                if (plus.text == "+") {
                                    val quantity = RapidEditText(this@AddIngredientsActivity)
                                    quantity.setEditTextNumber(decimal = true, signed = false)
                                    quantity.hint = "Quantità..."
                                    Dialog(this).showNormalDialogType(null,
                                        "Aggiungi quantità in " + IsiAppActivity.isiCashierRequest!!.transformIsimagaUnity(
                                            ingredient.unity_id
                                        ),
                                        null,
                                        MaterialTextAndListener("Ok") { dialogInterface: DialogInterface, _: Int ->
                                            try {
                                                val ingredients = Ingredients(
                                                    ingredient.id,
                                                    0,
                                                    quantity.textOrEmpty.toFloat()
                                                )
                                                ingredientsAdd!!.add(ingredients)
                                                plus.text = "-"
                                            } catch (e: Exception) {
                                                Toast.makeText(
                                                    this@AddIngredientsActivity,
                                                    "Formato non corretto",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            dialogInterface.dismiss()
                                        },
                                        null,
                                        quantity
                                    )
                                } else {
                                    for (i in ingredientsAdd!!) {
                                        if (i.product_forniture_id == ingredient.id) {
                                            ingredientsAdd!!.remove(i)
                                            break
                                        }
                                    }
                                    plus.text = "+"
                                }
                            }
                        }
                    }
                }
            }
        }.start()
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        val gson = Gson()
        resultIntent.putExtra("ingredients", gson.toJson(ingredientsAdd))
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}