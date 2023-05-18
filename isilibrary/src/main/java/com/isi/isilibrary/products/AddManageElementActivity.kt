package com.isi.isilibrary.products

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.isi.isiapi.classes.CategoryAndProduct
import com.isi.isiapi.classes.Ingredients
import com.isi.isiapi.classes.Product
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.products.ingredients.AddIngredientsActivity
import java.util.*
import kotlin.collections.ArrayList

class AddManageElementActivity : BackActivity() {
    private var products: Product? = null
    private var ingredients: MutableList<Ingredients?>? = null
    private var categoryDef = 0
    private var productDef = 0
    private var department = 1
    private lateinit var nametext: TextView
    private lateinit var priceText: TextView
    private lateinit var priceBancoText: TextView
    private lateinit var barcodeText: TextView
    private lateinit var descriptionElement: TextView
    private lateinit var categrySpinner: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_manage_element)

        val back = intent
        products = Gson().fromJson(back.getStringExtra("product"), Product::class.java)

        updateUI(R.layout.activity_add_manage_element)

    }

    override fun updateUI(layout: Int) {
        super.updateUI(layout)

        Thread {
            val categories: List<CategoryAndProduct>? =
                IsiAppActivity.httpRequest!!.categories
            if(ingredients == null)
                ingredients = IsiAppActivity.httpRequest!!.getProductIngredients(products)

            if (categories != null && ingredients != null)
            {
                runOnUiThread {
                    nametext = findViewById(R.id.modifyName)
                    priceText = findViewById(R.id.priceModify)
                    priceBancoText = findViewById(R.id.priceModifyBanco)
                    barcodeText = findViewById(R.id.barcodeElement)
                    descriptionElement = findViewById(R.id.descriptionElement)
                    categrySpinner = findViewById(R.id.addManageElementSpinner)

                    val productSpinner =
                        findViewById<AutoCompleteTextView>(R.id.product_connection_spinner)
                    val departmentSpinner =
                        findViewById<AutoCompleteTextView>(R.id.departmentCodeElement)
                    val chooseColor = findViewById<MaterialButton>(R.id.productColorButton)
                    val adapter =
                        ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    val productsArray: MutableList<Product> = ArrayList()
                    val fake = Product("Nessuno", 0f, 0f, 0, "", 0, 0, "", "", 0)
                    fake.id = 0
                    productsArray.add(0, fake)
                    for (cat in categories) {
                        productsArray.addAll(cat.product!!)
                    }
                    val adapterProduct =
                        ArrayAdapter(this, android.R.layout.simple_spinner_item, productsArray)
                    adapterProduct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    val adapterDepartment = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        arrayOf(1, 2, 3, 4)
                    )
                    adapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    categrySpinner.setAdapter(adapter)
                    productSpinner.setAdapter(adapterProduct)
                    departmentSpinner.setAdapter(adapterDepartment)

                    if (products != null) {
                        categoryDef = products!!.category_id
                        title = "Modifica elemento"
                        nametext.text = products!!.name
                        priceText.text = String.format(
                            Locale.getDefault(),
                            "%.2f",
                            products!!.price
                        )
                        priceBancoText.text = String.format(
                            Locale.getDefault(),
                            "%.2f",
                            products!!.price_banco
                        )
                        barcodeText.text = products!!.barcode_value
                        if (products!!.color != 0 && products!!.color != -1) chooseColor.setBackgroundColor(
                            products!!.color
                        )
                        for (cat in categories) {
                            if (cat.category?.id == products!!.category_id) {
                                categoryDef = cat.category!!.id
                                categrySpinner.setText(cat.category!!.name, false)
                                break
                            }
                        }
                        for (product in productsArray) {
                            if (products!!.connection_product != null) {
                                if (product.id == products!!.connection_product) {
                                    productSpinner.setText(product.name, false)
                                    productDef = product.id
                                    break
                                }
                            }
                        }
                        department = products!!.department
                        descriptionElement.text = products!!.description
                        departmentSpinner.setText(
                            String.format(
                                Locale.getDefault(),
                                "%d",
                                products!!.department
                            ), false
                        )
                    }
                    else {
                        title = "Aggiungi elemento"
                        productSpinner.setText("Nessuno", false)
                        departmentSpinner.setText("1", false)
                        try {
                            products = Product("", 0f, 0f, 1, "", 0, 0, "", "", 0)
                        } catch (e: Exception) {
                            AlertDialog.Builder(this@AddManageElementActivity)
                                .setTitle("Attenzione")
                                .setMessage("Devi prima creare una categoria")
                                .setCancelable(true)
                                .setNegativeButton("OK") { _: DialogInterface?, _: Int -> finish() }
                                .show()
                        }
                    }

                    categrySpinner.onItemClickListener =
                        OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                            categoryDef = categories[position].category!!.id
                        }
                    productSpinner.onItemClickListener =
                        OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                            productDef = productsArray[position].id
                        }
                    departmentSpinner.onItemClickListener =
                        OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                            department = position + 1
                        }
                    val addIngredients = findViewById<Button>(R.id.addIngredientsButton)
                    addIngredients.setOnClickListener {
                        val i = Intent(
                            this@AddManageElementActivity,
                            AddIngredientsActivity::class.java
                        )
                        i.putExtra("ingredients", Gson().toJson(ingredients))
                        ingredientsResult.launch(i)
                    }
                    chooseColor.setOnClickListener {
                        ColorPickerDialogBuilder
                            .with(this@AddManageElementActivity)
                            .setTitle("Scegli colore")
                            .initialColor(Color.WHITE)
                            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                            .density(12)
                            .setOnColorSelectedListener { selectedColor: Int ->
                                products!!.color = selectedColor
                                chooseColor.setBackgroundColor(selectedColor)
                            }
                            .setPositiveButton("ok") { _: DialogInterface?, _: Int, _: Array<Int?>? -> }
                            .setNegativeButton("cancella") { _: DialogInterface?, _: Int ->
                                products!!.color = 0
                            }
                            .build()
                            .show()
                    }
                }
            }
            else {
                errorPage(layout)
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
            nametext.error = null
            try {
                if (nametext.text.toString() == "" || nametext.text.toString()
                        .contains("-") || nametext.text.toString()
                        .contains(":") || nametext.text.toString()
                        .contains("!") || nametext.text.toString()
                        .contains("#") || nametext.text.toString().contains(",")
                ) {
                    nametext.error = "Il nome non può contenere caratteri speciali o essere vuoto"
                } else if (categoryDef == 0) {
                    categrySpinner.error = "Scegli una categoria"
                } else {

                    val price1 = priceText.text.toString().replace(",", ".")
                    val price2 = priceBancoText.text.toString().replace(",", ".")
                    val priceDouble = price1.toFloat()
                    val priceBancoDouble = price2.toFloat()

                    products!!.price = priceDouble
                    products!!.price_banco = priceBancoDouble
                    products!!.category_id = categoryDef
                    products!!.department = department
                    products!!.connection_product = if (productDef != 0) productDef else null
                    products!!.name = nametext.text.toString()
                    products!!.barcode_value = barcodeText.text.toString()
                    products!!.description = descriptionElement.text.toString()

                    if (products?.id == 0) {
                        Thread {
                            if (IsiAppActivity.httpRequest!!.addProduct(
                                    products,
                                    ingredients
                                )
                            ) {
                                runOnUiThread { finish() }
                            }
                        }.start()
                    }
                    else {
                        Thread {
                            if (IsiAppActivity.httpRequest!!.editProduct(
                                    products,
                                    ingredients
                                )
                            ) runOnUiThread { finish() }
                        }
                            .start()
                        finish()
                    }
                }
            } catch (ignore: Exception) {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val ingredientsResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val ingredients = result!!.data?.getStringExtra("ingredients")
        this.ingredients =
            Gson().fromJson(ingredients, object : TypeToken<ArrayList<Ingredients?>?>() {}.type)
    }

}