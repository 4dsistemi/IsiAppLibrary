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
    private var priority: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category_element)
        title = "Aggiungi categoria"
        category = findViewById(R.id.categoryElementEdit)
        priority = findViewById(R.id.priority_element)

        val backIntent = intent
        if (backIntent.getStringExtra("category") != null) {
            back = Gson().fromJson(backIntent.getStringExtra("category"), Category::class.java)
            title = "Modifica categoria"
            category!!.setText(back!!.name)
            priority!!.setText(back!!.priority.toString())
        }else{
            priority!!.setText("0")
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
                    back!!.priority = 0
                    priority!!.text.isNotEmpty().apply {
                        back!!.priority = Integer.parseInt(priority!!.text.toString())
                    }

                    Thread {
                        if (IsiAppActivity.httpRequest!!.editcategory(back)) {
                            runOnUiThread { finish() }
                        }
                    }.start()
                } else {
                    Thread {
                        var prio: Int
                        priority!!.text.isNotEmpty().apply {
                            prio = Integer.parseInt(priority!!.text.toString())
                        }

                        if (IsiAppActivity.httpRequest!!.addCategory(
                                Category(
                                    0,
                                    category!!.text.toString().trim { it <= ' ' },
                                    0,
                                    "default.png",
                                    prio
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