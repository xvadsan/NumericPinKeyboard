package com.xvadsan.numericpinkeyboard

import android.os.Bundle
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xvadsan.numericpinkeyboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() = with(binding) {
        etPin.doOnTextChanged { text, _, _, _ ->
            numericKeyboard.showFingerprint(text.toString().isEmpty())
            numericKeyboard.showDelete(text.toString().isNotEmpty())
        }
        etPin.setRawInputType(InputType.TYPE_CLASS_TEXT)
        etPin.setTextIsSelectable(true)
        val ic = etPin.onCreateInputConnection(EditorInfo())
        ic?.let { numericKeyboard.setInputConnection(it) }
        numericKeyboard.onFingerprintClicked = {
            Toast.makeText(this@MainActivity, "click fingerprint button", Toast.LENGTH_SHORT).show()
        }
        numericKeyboard.onForgotClicked = {
            Toast.makeText(this@MainActivity, "click forgot button", Toast.LENGTH_SHORT).show()
        }
    }
}