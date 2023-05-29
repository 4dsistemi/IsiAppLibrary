package com.isi.isilibrary.products

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.google.gson.Gson
import com.isi.isiapi.classes.CategoryAndProduct
import com.isi.isiapi.classes.Ingredients
import com.isi.isiapi.classes.Product
import com.isi.isiapi.classes.isimaga.ProductForniture
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity
import com.isi.isilibrary.dialog.Dialog
import com.isi.isilibrary.dialog.NetConnection
import com.isi.isilibrary.products.ingredients.AddIngredientsActivity
import java.util.*

class AddManageElementActivity : BackActivity() {
    private var products: Product? = null
    private var ingredients: MutableList<Ingredients>? = null
    private var productForniture: MutableList<ProductForniture?>? = null

    private var categoryDef = 0
    private var productDef = 0
    private var department = 1
    private lateinit var nametext: TextView
    private lateinit var priceText: TextView
    private lateinit var priceBancoText: TextView
    private lateinit var barcodeText: TextView
    private lateinit var descriptionElement: TextView
    private lateinit var categrySpinner: AutoCompleteTextView
    private lateinit var ingredientLinear : LinearLayout
    private lateinit var ingredientButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_manage_element)

        val back = intent
        products = Gson().fromJson(back.getStringExtra("product"), Product::class.java)

        updateUI(R.layout.activity_add_manage_element)

    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            if(data != null){
                if(data.getStringExtra("ingredient") != null){
                    val ingr = Gson().fromJson(data.getStringExtra("ingredient"), Ingredients::class.java)
                    ingredients!!.add(ingr)
                    updateIngredients()
                }
            }
        }
    }

    override fun updateUI(layout: Int) {
        super.updateUI(layout)

        val dial = Dialog(this).showLoadingDialog("Caricamento...")

        Thread {
            val categories: List<CategoryAndProduct>? =
                IsiAppActivity.httpRequest!!.categories

            productForniture = IsiAppActivity.httpRequest!!.isimagaGetProductForniture()

            if(ingredients == null)
                ingredients = IsiAppActivity.httpRequest!!.getProductIngredients(products)

            runOnUiThread { dial.dismiss() }

            if (categories != null && ingredients != null && productForniture != null)
            {
                runOnUiThread {
                    nametext = findViewById(R.id.modifyName)
                    priceText = findViewById(R.id.priceModify)
                    priceBancoText = findViewById(R.id.priceModifyBanco)
                    barcodeText = findViewById(R.id.barcodeElement)
                    descriptionElement = findViewById(R.id.descriptionElement)
                    categrySpinner = findViewById(R.id.addManageElementSpinner)

                    ingredientButton = findViewById(R.id.ingredient_add_button)
                    ingredientLinear = findViewById(R.id.ingredients_linear)


                    val productSpinner =
                        findViewById<AutoCompleteTextView>(R.id.product_connection_spinner)

                    val departmentSpinner =
                        findViewById<AutoCompleteTextView>(R.id.departmentCodeElement)

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

                    ingredientButton.setOnClickListener {
                        val intent = Intent(this, AddIngredientsActivity::class.java)
                        resultLauncher.launch(intent)
                    }

                    updateIngredients()

                    val chooseColor = findViewById<Button>(R.id.color_button)

                    if (products!!.color != 0 && products!!.color != -1) chooseColor.setBackgroundColor(
                        products!!.color
                    )

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

    private fun updateIngredients(){

        ingredientLinear.removeAllViews()

        for (ingr in ingredients!!){

            val view: View = LayoutInflater.from(this).inflate(R.layout.add_ingredients_layout, ingredientLinear, false)

            val pr = productForniture?.firstOrNull { it!!.id == ingr.product_forniture_id }

            val name = view.findViewById<TextView>(R.id.nameElementText)
            val addRemove = view.findViewById<Button>(R.id.buttonPlusLayout)
            val ingredientsQuantity = view.findViewById<TextView>(R.id.ingredients_quantity_text)

            ingredientsQuantity.text = String.format("%.4f %s", ingr.quantity, IsiAppActivity.httpRequest!!.transformIsimagaUnity(pr!!.unity_id))
            name.text = pr.name

            addRemove.setOnClickListener{
                ingredients!!.remove(ingr)

                updateIngredients()
            }

            ingredientLinear.addView(view)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.add_intestazione_menu, menu)

        val remove = menu.findItem(R.id.deleteIntestazioneDone);

        if(products == null){
            remove.isVisible = false
            remove.isEnabled = false
        }

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
                if (categoryDef == 0) {
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
        else if(id == R.id.deleteIntestazioneDone){
            Dialog(this).yesNoDialog("Attenzione", "Vuoi davvero eliminare questo elemento?",
                { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()

                    NetConnection(this, "Elimino prodotto", startNetConnection = {
                        products!!.active = -1
                        IsiAppActivity.httpRequest!!.editProduct(products, null)
                    },
                    onConnectionOk = {
                        if(it){
                            finish()
                        }else{
                            Dialog(this).showCustomErrorConnectionDialog("Errore nell'eliminazione del prodotto")
                        }
                    },
                    onConnectionError = {

                    }).start()

                }, null)
        }

        return super.onOptionsItemSelected(item)
    }

}