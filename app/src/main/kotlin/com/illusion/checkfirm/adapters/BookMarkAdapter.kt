package com.illusion.checkfirm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.BookMark

class BookMarkAdapter(private val bookmarkList: List<BookMark>): RecyclerView.Adapter<BookMarkAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.name)
        var model: TextView = view.findViewById(R.id.model)
        var csc: TextView = view.findViewById(R.id.csc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_items, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bookmark = bookmarkList[position]

        holder.name.text = bookmark.name
        holder.model.text = bookmark.model
        holder.csc.text = bookmark.csc
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }
}