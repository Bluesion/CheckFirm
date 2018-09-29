package com.tanzentlab.checksamfirm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tanzentlab.checksamfirm.R
import com.tanzentlab.checksamfirm.database.BookMark

class FastBookMarkAdapter(private val bookmarkList: List<BookMark>): RecyclerView.Adapter<FastBookMarkAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fast_bookmark_items, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bookmark = bookmarkList[position]

        holder.name.text = bookmark.name
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }
}