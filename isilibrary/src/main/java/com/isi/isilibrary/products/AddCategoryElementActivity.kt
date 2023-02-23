package com.isi.isilibrary.products

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.isi.isiapi.classes.Category
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.backActivity.BackActivity

class AddCategoryElementActivity : BackActivity() {
    private var back: Category? = null
    private var category: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category_element)
        title = "Aggiungi categoria"
        category = findViewById(R.id.categoryElementEdit)
        val backIntent = intent
        if (backIntent.getStringExtra("category") != null) {
            back = Gson().fromJson(backIntent.getStringExtra("category"), Category::class.java)
            title = "Modifica categoria"
            category!!.setText(back!!.name)
        }
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
            if (category!!.text.toString().trim { it <= ' ' } == "") {
                Toast.makeText(this, "Il nome non può essere vuoto", Toast.LENGTH_LONG).show()
            } else {
                if (back != null) {
                    back!!.image = "default.png"
                    back!!.name = category!!.text.toString().trim { it <= ' ' }
                    Thread {
                        if (IsiAppActivity.httpRequest!!.editcategory(back)) {
                            runOnUiThread { finish() }
                        }
                    }.start()
                } else {
                    Thread {
                        if (IsiAppActivity.httpRequest!!.addCategory(
                                Category(
                                    0,
                                    category!!.text.toString().trim { it <= ' ' },
                                    0,
                                    "default.png"
                                )
                            )
                        ) {
                            runOnUiThread { finish() }
                        }
                    }.start()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}