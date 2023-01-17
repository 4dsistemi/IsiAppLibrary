package com.isi.isilibrary.backActivity

import android.view.MenuItem
import android.widget.Button
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.dialog.Dialog

open class BackActivity : AppCompatActivity() {
    private var pDialog: AlertDialog? = null

    override fun onResume() {
        super.onResume()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    @CallSuper
    open fun updateUI(layout: Int) {
        runOnUiThread { setContentView(layout) }
    }

    fun errorPage(layout: Int) {
        runOnUiThread {
            setContentView(R.layout.error_data)
            val reloadButton = findViewById<Button>(R.id.reload_data_button)
            reloadButton.setOnClickListener { updateUI(layout) }
        }
    }

    fun emptyData() {
        runOnUiThread { setContentView(R.layout.empty_data) }
    }

    fun startLoader(title: String?) {
        val titleIn = title ?: "Aggiorno dati..."
        runOnUiThread { pDialog = Dialog(this).showLoadingDialog(titleIn) }
    }

    fun stopLoader() {
        runOnUiThread {
            if (pDialog != null) {
                pDialog!!.dismiss()
            }
        }
    }
}