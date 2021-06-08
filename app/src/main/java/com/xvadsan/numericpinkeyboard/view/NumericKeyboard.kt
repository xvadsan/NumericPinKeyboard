package com.xvadsan.numericpinkeyboard.view

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputConnection
import androidx.constraintlayout.widget.ConstraintLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xvadsan.numericpinkeyboard.R
import com.xvadsan.numericpinkeyboard.databinding.NumericKeyboardBinding

class NumericKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private val binding by viewBinding(NumericKeyboardBinding::bind)
    var keyValues = SparseArray<String>()
    var inputConnection: InputConnection? = null
    var onForgotClicked: (() -> Unit)? = null
    var onFingerprintClicked: (() -> Unit?)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.numeric_keyboard, this)
        with(binding) {
            viewOne.setOnClickListener(this@NumericKeyboard)
            viewTwo.setOnClickListener(this@NumericKeyboard)
            viewThree.setOnClickListener(this@NumericKeyboard)
            viewFour.setOnClickListener(this@NumericKeyboard)
            viewFive.setOnClickListener(this@NumericKeyboard)
            viewSix.setOnClickListener(this@NumericKeyboard)
            viewSeven.setOnClickListener(this@NumericKeyboard)
            viewEight.setOnClickListener(this@NumericKeyboard)
            viewNine.setOnClickListener(this@NumericKeyboard)
            viewZero.setOnClickListener(this@NumericKeyboard)
            viewDelete.setOnClickListener(this@NumericKeyboard)
            keyValues.put(R.id.view_one, resources.getString(R.string.numericKeyboardOne))
            keyValues.put(R.id.view_two, resources.getString(R.string.numericKeyboardTwo))
            keyValues.put(R.id.view_three, resources.getString(R.string.numericKeyboardThree))
            keyValues.put(R.id.view_four, resources.getString(R.string.numericKeyboardFour))
            keyValues.put(R.id.view_five, resources.getString(R.string.numericKeyboardFive))
            keyValues.put(R.id.view_six, resources.getString(R.string.numericKeyboardSix))
            keyValues.put(R.id.view_seven, resources.getString(R.string.numericKeyboardSeven))
            keyValues.put(R.id.view_eight, resources.getString(R.string.numericKeyboardEight))
            keyValues.put(R.id.view_nine, resources.getString(R.string.numericKeyboardNine))
            keyValues.put(R.id.view_zero, resources.getString(R.string.numericKeyboardZero))
            viewForgot.onClick {
                onForgotClicked?.invoke()
            }
            viewFingerprint.onClick {
                onFingerprintClicked?.invoke()
            }
        }
    }

    override fun onClick(view: View) {
        if (inputConnection == null) return
        when (view.id) {
            R.id.view_delete -> {
                val selectedText = inputConnection?.getSelectedText(0)
                if (selectedText.isNullOrEmpty()) {
                    inputConnection?.deleteSurroundingText(1, 0)
                } else {
                    inputConnection?.commitText("", 1)
                }
            }
            else -> {
                val value = keyValues[view.id]
                inputConnection?.commitText(value, 1)
            }
        }
    }

    @JvmName("setInputConnection1")
    fun setInputConnection(ic: InputConnection) {
        inputConnection = ic
    }

    fun showDelete(show: Boolean) {
        binding.gDelete.showIf { show }
    }

    fun showForgot(show: Boolean) {
        binding.gForgot.showIf { show }
    }

    fun showFingerprint(show: Boolean) {
        binding.gFingerprint.showIf { show }
    }
}