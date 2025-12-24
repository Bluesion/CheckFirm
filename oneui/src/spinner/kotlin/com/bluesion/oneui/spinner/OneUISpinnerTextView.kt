package com.bluesion.oneui.spinner

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.Checkable
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textview.MaterialTextView

class OneUISpinnerTextView : MaterialTextView, Checkable {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private var isChecked = false

    init {
        gravity = Gravity.CENTER_VERTICAL
        minHeight = dpToPx(48F)
        setPadding(dpToPx(16F), 0, dpToPx(16F), 0)
    }

    override fun setChecked(checked: Boolean) {
        isChecked = checked
        if (checked) {
            setTextColor(ResourcesCompat.getColor(resources, com.bluesion.oneui.R.color.oneui_primary, context.theme))
            setCompoundDrawablesWithIntrinsicBounds(0, 0, com.bluesion.oneui.R.drawable.oneui_spinner_icon_check, 0)
        } else {
            setTextColor(ResourcesCompat.getColor(resources, com.bluesion.oneui.R.color.oneui_onSurface, context.theme))
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    private fun dpToPx(dp: Float): Int {
        return (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }
}