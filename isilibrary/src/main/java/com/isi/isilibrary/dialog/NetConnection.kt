package com.isi.isilibrary.dialog

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class NetConnection<E>(
        private val c: AppCompatActivity,
        private val loading: String?,
        private val startNetConnection: () -> E,
        private val onConnectionOk: (E) -> Unit,
        private val onConnectionError: () -> Unit,
        ) {

    fun start() {
        var dialog: AlertDialog? = null
        if (loading != null) {
            dialog = Dialog(c).showLoadingDialog(loading)
        }
        val finalDialog = dialog
        Thread {
            val conn: E = startNetConnection()
            c.runOnUiThread {
                finalDialog?.dismiss()
                if (conn == null) {
                    Dialog(c).showErrorConnectionDialog(false)
                    onConnectionError()
                } else {
                    onConnectionOk(conn)
                }
            }
        }.start()
    }
}