package com.illusion.checkfirm.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.BookmarkDB

class BookmarkAdapter(private var bookmarkList: List<BookmarkDB>, val onClickListener: MyAdapterListener): RecyclerView.Adapter<BookmarkAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val edit: ImageButton = view.findViewById(R.id.edit)
        private val delete: ImageButton = view.findViewById(R.id.delete)
        var name: TextView = view.findViewById(R.id.name)
        var model: TextView = view.findViewById(R.id.model)
        var csc: TextView = view.findViewById(R.id.csc)

        init {
            edit.setOnClickListener { v -> onClickListener.onEditClicked(v, adapterPosition) }
            delete.setOnClickListener { v -> onClickListener.onDeleteClicked(v, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_bookmark_items, parent, false)

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

    interface MyAdapterListener {
        fun onEditClicked(v: View, position: Int)
        fun onDeleteClicked(v: View, position: Int)
    }
}