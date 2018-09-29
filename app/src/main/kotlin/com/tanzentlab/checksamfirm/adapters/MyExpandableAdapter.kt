package com.tanzentlab.checksamfirm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.tanzentlab.checksamfirm.R
import java.util.ArrayList
import java.util.HashMap

class MyExpandableAdapter(private val context: Context, private val header: List<String>, private val child: HashMap<String, ArrayList<String>>) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return this.header.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return this.child[this.header[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return this.header[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return this.child[this.header[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition) as String

        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.header, parent, false)
        }

        val header = convertView!!.findViewById<TextView>(R.id.header)
        header.text = headerTitle
        val icon = convertView.findViewById<ImageView>(R.id.icon)
        icon.isSelected = isExpanded

        return convertView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val childText = getChild(groupPosition, childPosition) as String

        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.items, parent, false)
        }

        val child = convertView!!.findViewById<TextView>(R.id.items)

        child.text = childText
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}