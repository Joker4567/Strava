package com.skillbox.core.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.OnTouchListener(void: () -> Unit) {
    this.setOnTouchListener { v, event ->
        if (event?.action === KeyEvent.ACTION_DOWN) {
            void.invoke()
            true
        }
        false
    }
}