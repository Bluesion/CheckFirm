package com.bluesion.oneui.spinner

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class OneUISpinner : AppCompatSpinner {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    init {
        adapter = OneUISpinnerAdapter(context)
    }

    fun setList(list: List<String>) {
        (adapter as OneUISpinnerAdapter).setList(list)
    }
}