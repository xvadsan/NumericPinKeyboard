package com.xvadsan.numericpinkeyboard.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.xvadsan.numericpinkeyboard.R

class AccessCodeEditText : AppCompatEditText{

    private lateinit var pinPainter: PinPainter

    private var onClickListener: OnClickListener? = null
    private var onEditorActionListener: OnEditorActionListener? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            val index =  text?.let {
                it.length
            } ?: 0
            setSelection(index)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        this.onClickListener = onClickListener
    }

    override fun setOnEditorActionListener(onEditorActionListener: OnEditorActionListener) {
        this.onEditorActionListener = onEditorActionListener
    }

    @Suppress("FunctionOnlyReturningConstant")
    fun canPaste(): Boolean = false

    override fun isSuggestionsEnabled(): Boolean = false

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val (newWidthMeasureSpec, newHeightMeasureSpec) = pinPainter.getCalculatedMeasureSpecSize()
        setMeasuredDimension(newWidthMeasureSpec, newHeightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        pinPainter.draw(canvas)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        isCursorVisible = false
        isLongClickable = false
        maxLines = DEFAULT_PIN_MAX_LINES
        setBackgroundColor(Color.TRANSPARENT)

        initClickListener()
        initOnEditorActionListener()

        var normalStateDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.normal_state_background)
        var highlightStateDrawable: Drawable? = ContextCompat.getDrawable(context, R.drawable.highlight_state_background)
        var normalPinWidth = context.dpToPx(DEFAULT_PIN_WIDTH)
        var normalPinHeight = context.dpToPx(DEFAULTL_PIN_HEIGHT)
        var pinTotal = DEFAULT_PIN_TOTAL
        var pinSpace = context.dpToPx(DEFAULT_PIN_SPACE)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AccessCodeEditText, defStyleAttr, 0)

            try {
                pinTotal = getTextViewMaxLength(attrs, pinTotal)
                normalPinWidth = typedArray.getDimension(R.styleable.AccessCodeEditText_pinWidth, normalPinWidth)
                normalPinHeight = typedArray.getDimension(R.styleable.AccessCodeEditText_pinHeight, normalPinHeight)
                pinSpace = typedArray.getDimension(R.styleable.AccessCodeEditText_pinSpace, pinSpace)


                typedArray.getDrawable(R.styleable.AccessCodeEditText_pinNormalStateDrawable)?.let {
                    normalStateDrawable = it
                }
                typedArray.getDrawable(R.styleable.AccessCodeEditText_pinHighlightStateDrawable)?.let {
                    highlightStateDrawable = it
                }

            } finally {
                typedArray.recycle()
            }

        }

        require(normalStateDrawable != null) {
            "normalStateDrawable must not be null"
        }

        require(highlightStateDrawable != null) {
            "highlightStateDrawable must not be null"
        }

        pinPainter = PinPainter(
            normalStateDrawable!!,
            highlightStateDrawable!!,
            this,
            normalPinWidth,
            normalPinHeight,
            pinTotal,
            pinSpace)
    }

    private fun initClickListener() {
        super.setOnClickListener { view ->
            // Force the cursor to the end every time we click at this EditText
            val index =  text?.let {
                it.length
            } ?: 0
            setSelection(index)
            onClickListener?.onClick(view)
        }
    }

    private fun initOnEditorActionListener() {
        super.setOnEditorActionListener { view, actionId, event ->
            // For some reasons the soft keyboard does not response when tap :(
            // But after set OnEditorActionListener it works :)
            onEditorActionListener?.onEditorAction(view, actionId, event) ?: false
        }
    }

    private fun getTextViewMaxLength(attrs: AttributeSet, defaultMaxLength: Int): Int {
        return attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", defaultMaxLength)
    }

    private fun Context.dpToPx(dp: Int) = dp * resources.displayMetrics.density

    companion object {
        private const val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"
        private const val DEFAULT_PIN_WIDTH = 16
        private const val DEFAULTL_PIN_HEIGHT = 16
        private const val DEFAULT_PIN_TOTAL = 4
        private const val DEFAULT_PIN_SPACE = 32
        private const val DEFAULT_PIN_MAX_LINES = 1
    }
}