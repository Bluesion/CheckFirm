package com.illusion.checkfirm.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.illusion.checkfirm.R
import java.util.ArrayList

class ListAdapter internal constructor(private val context: Activity, private val firmware: ArrayList<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return firmware.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    private inner class ViewHolder {
        internal var text: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        val inflater = context.layoutInflater

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items, null)
            holder = ViewHolder()
            holder.text = convertView!!.findViewById(R.id.firmware)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.text!!.text = firmware[position]
        return convertView
    }
}