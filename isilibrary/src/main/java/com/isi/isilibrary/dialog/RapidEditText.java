package com.isi.isilibrary.dialog;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

public class RapidEditText extends TextInputEditText {

    public RapidEditText(@NonNull Context context) {
        super(context);
    }

    public RapidEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RapidEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setEditTextMail(){
        setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    public void setEditTextNumber(boolean decimal, boolean signed){

        int type = InputType.TYPE_CLASS_NUMBER;

        if(decimal){
            type = type | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        }

        if(signed){
            type = type | InputType.TYPE_NUMBER_FLAG_SIGNED;
        }

        setInputType(type);
    }

    public String getTextOrEmpty(){
        if(this.getText() == null){
            return "";
        }else{
         return this.getText().toString();
        }
    }
}
