package com.isi.isilibrary.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isi.isilibrary.R

class Dialog {
    enum class DIALOG_TYPE {
        ERROR_TYPE, WARNING_TYPE, SUCCESS_TYPE
    }

    private val c: Context
    private var alertDialogBuilder: MaterialAlertDialogBuilder

    constructor(c: Context) {
        this.c = c
        alertDialogBuilder = MaterialAlertDialogBuilder(c)
    }

    constructor(fragment: Fragment) {
        c = fragment.requireActivity()
        alertDialogBuilder = MaterialAlertDialogBuilder(c)
    }

    fun showNormalDialogType(
        dialog_type: DIALOG_TYPE?,
        title: String,
        message: String?,
        confirm: MaterialTextAndListener?,
        reject: MaterialTextAndListener?
    )  : AlertDialog{
        alertDialogBuilder.setTitle(title)
        if (message != null) {
            alertDialogBuilder.setMessage(message)
        }
        if (dialog_type != null) {
            when (dialog_type) {
                DIALOG_TYPE.ERROR_TYPE -> alertDialogBuilder.setIcon(R.drawable.error)
                DIALOG_TYPE.SUCCESS_TYPE -> alertDialogBuilder.setIcon(R.drawable.success)
                DIALOG_TYPE.WARNING_TYPE -> alertDialogBuilder.setIcon(R.drawable.warning)
            }
        }
        if (confirm != null) {
            alertDialogBuilder.setPositiveButton(confirm.description, confirm.clickListener)
        }
        if (reject != null) {
            alertDialogBuilder.setNegativeButton(reject.description, reject.clickListener)
        }
        return alertDialogBuilder.show()
    }

    fun showNormalDialogType(
        dialog_type: DIALOG_TYPE?,
        title: String,
        message: String?,
        confirm: MaterialTextAndListener?,
        reject: MaterialTextAndListener?,
        view: View,
        cancable: Boolean
    ) : AlertDialog {
        val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val inflate = inflater.inflate(R.layout.dialog_custom_view, null)
        val layout = inflate.findViewById<LinearLayout>(R.id.dialog_custom_view_linear)
        layout.addView(view)
        alertDialogBuilder.setView(inflate)
            .setTitle(title)
        if (message != null) {
            alertDialogBuilder.setMessage(message)
        }
        if (dialog_type != null) {
            when (dialog_type) {
                DIALOG_TYPE.ERROR_TYPE -> alertDialogBuilder.setIcon(R.drawable.error)
                DIALOG_TYPE.SUCCESS_TYPE -> alertDialogBuilder.setIcon(R.drawable.success)
                DIALOG_TYPE.WARNING_TYPE -> alertDialogBuilder.setIcon(R.drawable.warning)
            }
        }
        if (confirm != null) {
            alertDialogBuilder.setPositiveButton(confirm.description, confirm.clickListener)
        }
        if (reject != null) {
            alertDialogBuilder.setNegativeButton(reject.description, reject.clickListener)
        }

        alertDialogBuilder.setCancelable(cancable)
        return alertDialogBuilder.show()

    }

    fun showErrorConnectionDialog(finish: Boolean) : AlertDialog{
        return MaterialAlertDialogBuilder(c)
            .setTitle("Attenzione")
            .setIcon(R.drawable.error)
            .setPositiveButton("Ok") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                if (finish) {
                    val appCompatActivity = c as AppCompatActivity
                    appCompatActivity.finish()
                }
            }
            .setMessage("Errore di connessione")
            .show()
    }

    fun showCustomErrorConnectionDialog(message: String?) : AlertDialog{
        return MaterialAlertDialogBuilder(c)
            .setTitle("Attenzione")
            .setIcon(R.drawable.error)
            .setPositiveButton("Ok") { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }
            .setMessage(message)
            .show()
    }

    fun showSuccessDialog(title: String?, message: String?) : AlertDialog {
        return MaterialAlertDialogBuilder(c)
            .setTitle(title)
            .setIcon(R.drawable.success)
            .setPositiveButton("Ok") { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }
            .setMessage(message)
            .show()
    }

    fun showLoadingDialog(title: String?): AlertDialog {
        val inflaterPerso = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val inflatePerso = inflaterPerso.inflate(R.layout.progress_loader_dialog, null)
        return MaterialAlertDialogBuilder(c)
            .setTitle(title)
            .setView(inflatePerso)
            .setCancelable(false)
            .show()
    }

    fun yesNoDialog(
        title: String,
        message: String?,
        yes: DialogInterface.OnClickListener,
        no: DialogInterface.OnClickListener?
    )  : AlertDialog{
        val builder = MaterialAlertDialogBuilder(c).setTitle(title)
        if (message != null) {
            builder.setMessage(message)
        }
        builder.setPositiveButton("Sì", yes)
        if (no != null) {
            builder.setNegativeButton("No", no)
        } else {
            builder.setNegativeButton("No") { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }
        }
        return builder.show()
    }
}