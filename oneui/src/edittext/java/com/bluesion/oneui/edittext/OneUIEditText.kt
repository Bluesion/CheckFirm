package com.bluesion.oneui.edittext

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.bluesion.oneui.R
import com.bluesion.oneui.databinding.LayoutOneuiEditTextBinding
import com.google.android.material.card.MaterialCardView

class OneUIEditText : MaterialCardView {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
        initView()
    }

    private val binding: LayoutOneuiEditTextBinding by lazy {
        LayoutOneuiEditTextBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.layout_oneui_edit_text, this, false)
        )
    }

    init {
        setCardBackgroundColor(null)
    }

    private var hint = ""
    @ColorInt private var hintTextColor = 0
    private var inputText = ""
    private var textAppearance = 0

    private var inputType = 0
    private var maxLines = 0
    private var privateImeOptions = ""
    private var imeOptions = 1
    private var importantForAutofill = 0

    private var clearIcon = 0
    private var isClearIconEnabled = false
    @ColorInt private var lineColor = 0
    @Px private var lineHeight = 0

    private fun initAttrs(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.OneUIEditText, 0, 0
        ).apply {
            try {
                hint = getString(R.styleable.OneUIEditText_hint) ?: ""
                hintTextColor = getColor(R.styleable.OneUIEditText_hintTextColor, 0)
                inputText = getString(R.styleable.OneUIEditText_text) ?: ""
                textAppearance = getResourceId(R.styleable.OneUIEditText_textAppearance, 0)
                inputType = getInt(R.styleable.OneUIEditText_inputType, 0)
                maxLines = getInt(R.styleable.OneUIEditText_maxLines, 0)
                privateImeOptions = getString(R.styleable.OneUIEditText_privateImeOptions) ?: ""
                imeOptions = getInt(R.styleable.OneUIEditText_imeOptions, 1)
                importantForAutofill = getInt(R.styleable.OneUIEditText_importantForAutofill, 0)
                clearIcon = getResourceId(R.styleable.OneUIEditText_clearIcon, 0)
                isClearIconEnabled = getBoolean(R.styleable.OneUIEditText_enableClearIcon, false)
                lineColor = getColor(R.styleable.OneUIEditText_lineColor, 0)
                lineHeight = getDimensionPixelSize(R.styleable.OneUIEditText_lineHeight, R.dimen.oneui_edit_text_underline_height)
            } finally {
                recycle()
            }
        }
    }

    private fun initView() {
        addView(binding.root)
        initText()
        initOption()
        initClearIcon()
        initLine()
    }

    private fun initText() {
        if (hint.isNotEmpty()) {
            binding.editText.hint = hint
        }

        if (hintTextColor > 0) {
            binding.editText.setHintTextColor(hintTextColor)
        }

        if (inputText.isNotEmpty()) {
            binding.editText.setText(inputText)
        }

        if (textAppearance > 0) {
            binding.editText.setTextAppearance(textAppearance)
        }
    }

    private fun initOption() {
        binding.editText.inputType = inputType
        binding.editText.maxLines = maxLines
        if (privateImeOptions.isNotEmpty()) {
            binding.editText.privateImeOptions = privateImeOptions
        }
        binding.editText.imeOptions = imeOptions
        binding.editText.importantForAutofill = importantForAutofill
    }

    private fun initClearIcon() {
        if (isClearIconEnabled) {
            binding.clearIcon.visibility = View.VISIBLE
            if (clearIcon > 0) {
                binding.clearIcon.setImageResource(clearIcon)
            }
            binding.clearIcon.setOnClickListener {
                binding.editText.text?.clear()
            }
            binding.editText.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun afterTextChanged(s: android.text.Editable?) {
                    if (s.isNullOrEmpty()) {
                        binding.clearIcon.visibility = View.GONE
                    } else {
                        binding.clearIcon.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

    private fun initLine() {
        if (lineColor > 0) {
            binding.underline.dividerColor = lineColor
        }
        binding.underline.dividerThickness = lineHeight
    }

    private fun dpToPx(dp: Float): Float {
        return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun setSelection(index: Int) {
        binding.editText.setSelection(index)
    }

    fun setText(text: String) {
        inputText = text
        binding.editText.setText(text)
    }

    fun getText(): String {
        return binding.editText.text!!.trim().toString()
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        binding.editText.addTextChangedListener(watcher)
    }
}