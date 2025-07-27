package com.bluesion.oneui.tab

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import com.bluesion.oneui.R
import com.bluesion.oneui.databinding.LayoutOneuiTabBinding
import com.google.android.material.card.MaterialCardView

class OneUITab : MaterialCardView {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
        initView()
    }

    init {
        val dpToPx8 = dpToPx(8F).toInt()
        val dpToPx12 = dpToPx(12F).toInt()
        val dpToPx26 = dpToPx(26F)

        isClickable = true
        isFocusable = true
        radius = dpToPx26
        strokeWidth = 0
        setContentPadding(dpToPx12, dpToPx8, dpToPx12, dpToPx8)
        minimumHeight = dpToPx26.toInt()
    }

    private val binding: LayoutOneuiTabBinding by lazy {
        LayoutOneuiTabBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.layout_oneui_tab, this, false)
        )
    }

    private var isSubTab = false
    private var tabTitle = ""
    private var isTabSelected = false
    private var customFontFamily = 0

    private fun initAttrs(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.OneUITab, 0, 0
        ).apply {
            try {
                isSubTab = getBoolean(R.styleable.OneUITab_isSubTab, false)
                tabTitle = getString(R.styleable.OneUITab_tabTitle) ?: ""
                isTabSelected = getBoolean(R.styleable.OneUITab_isTabSelected, false)
                customFontFamily = getResourceId(R.styleable.OneUITab_customFontFamily, 0)
            } finally {
                recycle()
            }
        }
    }

    private fun initView() {
        addView(binding.root)
        binding.tabTitle.text = tabTitle
        setTabSelected(isTabSelected)
    }

    fun setTabSelected(isSelected: Boolean) {
        if (isSelected) {
            if (isSubTab) {
                setCardBackgroundColor(context.getColor(R.color.oneui_sub_tab_background_selected))
                binding.tabIndicator.visibility = GONE
            } else {
                setCardBackgroundColor(context.getColor(R.color.oneui_sub_tab_background))
                binding.tabIndicator.visibility = VISIBLE
            }
            binding.tabTitle.let {
                if (customFontFamily > 0) {
                    it.setTypeface(Typeface.create(ResourcesCompat.getFont(context, customFontFamily), Typeface.BOLD))
                } else {
                    it.setTypeface(null, Typeface.BOLD)
                }
                it.setTextColor(ResourcesCompat.getColor(resources, R.color.oneui_tab_text_selected, context.theme))
            }
        } else {
            setCardBackgroundColor(context.getColor(R.color.oneui_sub_tab_background))
            binding.tabIndicator.visibility = if (isSubTab) GONE else INVISIBLE
            binding.tabTitle.let {
                if (customFontFamily > 0) {
                    it.setTypeface(Typeface.create(ResourcesCompat.getFont(context, customFontFamily), Typeface.NORMAL))
                } else {
                    it.setTypeface(null, Typeface.NORMAL)
                }
                it.setTextColor(ResourcesCompat.getColor(resources, R.color.oneui_tab_text, context.theme))
            }
        }
    }

    private fun dpToPx(dp: Float): Float {
        return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
    }
}