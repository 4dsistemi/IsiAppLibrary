package com.isi.isilibrary.dialog

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class RapidEditText : TextInputEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setEditTextMail() {
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    fun setEditTextNumber(decimal: Boolean, signed: Boolean) {
        var type = InputType.TYPE_CLASS_NUMBER
        if (decimal) {
            type = type or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        if (signed) {
            type = type or InputType.TYPE_NUMBER_FLAG_SIGNED
        }
        inputType = type
    }

    val textOrEmpty: String
        get() = if (this.text == null) {
            ""
        } else {
            this.text.toString()
        }
}