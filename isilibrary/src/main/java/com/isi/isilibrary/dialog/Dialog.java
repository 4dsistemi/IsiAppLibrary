package com.isi.isilibrary.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.isi.isilibrary.R;

public class Dialog {

    public enum DIALOG_TYPE{
        ERROR_TYPE,
        WARNING_TYPE,
        SUCCESS_TYPE
    }

    private final Context c;
    private final MaterialAlertDialogBuilder alertDialogBuilder;

    public Dialog(Context c) {
        this.c = c;
        alertDialogBuilder =  new MaterialAlertDialogBuilder(c);
    }

    public Dialog(Fragment fragment) {
        this.c = fragment.requireActivity();
        alertDialogBuilder =  new MaterialAlertDialogBuilder(c);
    }

    public void showNormalDialogType(DIALOG_TYPE dialog_type, @NonNull String title, String message, MaterialTextAndListener confirm, MaterialTextAndListener reject){
        alertDialogBuilder.setTitle(title);

        if(message != null){
            alertDialogBuilder.setMessage(message);
        }

        if(dialog_type != null){
            switch (dialog_type){
                case ERROR_TYPE:
                    alertDialogBuilder.setIcon(R.drawable.error);
                    break;
                case SUCCESS_TYPE:
                    alertDialogBuilder.setIcon(R.drawable.success);
                    break;
                case WARNING_TYPE:
                    alertDialogBuilder.setIcon(R.drawable.warning);
                    break;
            }
        }

        if(confirm != null){
            alertDialogBuilder.setPositiveButton(confirm.getDescription(), confirm.getClickListener());
        }

        if(reject != null){
            alertDialogBuilder.setNegativeButton(reject.getDescription(), reject.getClickListener());
        }

        alertDialogBuilder.show();

    }

    public void showNormalDialogType(DIALOG_TYPE dialog_type, @NonNull String title, String message, MaterialTextAndListener confirm, MaterialTextAndListener reject, @NonNull View view){
        alertDialogBuilder.setView(view)
                .setTitle(title);

        if(message != null){
            alertDialogBuilder.setMessage(message);
        }

        if(dialog_type != null){
            switch (dialog_type){
                case ERROR_TYPE:
                    alertDialogBuilder.setIcon(R.drawable.error);
                    break;
                case SUCCESS_TYPE:
                    alertDialogBuilder.setIcon(R.drawable.success);
                    break;
                case WARNING_TYPE:
                    alertDialogBuilder.setIcon(R.drawable.warning);
                    break;
            }
        }

        if(confirm != null){
            alertDialogBuilder.setPositiveButton(confirm.getDescription(), confirm.getClickListener());
        }

        if(reject != null){
            alertDialogBuilder.setNegativeButton(reject.getDescription(), reject.getClickListener());
        }

        alertDialogBuilder.show();

    }

    public void showErrorConnectionDialog(boolean finish){
        new MaterialAlertDialogBuilder(c)
                .setTitle("Attenzione")
                .setIcon(R.drawable.error)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    if(finish){
                        AppCompatActivity appCompatActivity = (AppCompatActivity)c;
                        appCompatActivity.finish();
                    }
                })
                .setMessage("Errore di connessione")
                .show();
    }

    public void showCustomErrorConnectionDialog(String message){
        new MaterialAlertDialogBuilder(c)
                .setTitle("Attenzione")
                .setIcon(R.drawable.error)
                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss())
                .setMessage(message)
                .show();
    }

    public void showSuccessDialog(String title, String message){
        new MaterialAlertDialogBuilder(c)
                .setTitle(title)
                .setIcon(R.drawable.success)
                .setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss())
                .setMessage(message)
                .show();
    }

    public AlertDialog showLoadingDialog(String title){

        LayoutInflater inflaterPerso = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View inflatePerso = inflaterPerso.inflate(R.layout.progress_loader_dialog, null);

        return new MaterialAlertDialogBuilder(c)
                .setTitle(title)
                .setView(inflatePerso)
                .setCancelable(false)
                .show();

    }

    public void yesNoDialog(@NonNull String title, String message, @NonNull DialogInterface.OnClickListener yes, DialogInterface.OnClickListener no){

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(c).setTitle(title);

        if(message != null){
            builder.setMessage(message);
        }

        builder.setPositiveButton("Sì", yes);

        if(no != null){
            builder.setNegativeButton("No", no);
        }else{
            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        }

        builder.show();

    }

}

