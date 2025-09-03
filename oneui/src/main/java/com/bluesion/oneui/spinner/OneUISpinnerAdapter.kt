package com.bluesion.oneui.spinner

import android.content.Context
import android.widget.ArrayAdapter
import com.bluesion.oneui.R

class OneUISpinnerAdapter(context: Context) :
    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mutableListOf<String>()) {

    init {
        setDropDownViewResource(R.layout.layout_oneui_spinner_item)
    }

    fun setList(list: List<String>) {
        clear()
        addAll(list)
        notifyDataSetChanged()
    }
}