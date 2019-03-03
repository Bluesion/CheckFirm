package com.illusion.checkfirm.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.BookmarkDB

class FastBookmarkAdapter(private val bookmarkList: List<BookmarkDB>): RecyclerView.Adapter<FastBookmarkAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_fast_bookmark_items, parent, false)

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