package com.bluesion.oneui.divider

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import com.bluesion.oneui.R

class OneUIDivider : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        setBackgroundResource(R.drawable.oneui_drawer_divider)
        minimumHeight =
            (12F * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }
}