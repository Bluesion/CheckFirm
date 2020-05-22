package com.illusion.checkfirm.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkEntity

class BookmarkAdapter(private var bookmarkList: List<BookmarkEntity>,
                      val onClickListener: MyAdapterListener) : RecyclerView.Adapter<BookmarkAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val layout: ConstraintLayout = view.findViewById(R.id.bookmark_layout)
        private val edit: ImageButton = view.findViewById(R.id.edit)
        private val delete: ImageButton = view.findViewById(R.id.delete)
        var name: MaterialTextView = view.findViewById(R.id.name)
        var model: MaterialTextView = view.findViewById(R.id.model)
        var csc: MaterialTextView = view.findViewById(R.id.csc)

        init {
            layout.setOnClickListener {
                onClickListener.onLayoutClicked(
                        bookmarkList[bindingAdapterPosition].model,
                        bookmarkList[bindingAdapterPosition].csc
                )
            }
            edit.setOnClickListener {
                onClickListener.onEditClicked(
                        bookmarkList[bindingAdapterPosition].id!!,
                        bookmarkList[bindingAdapterPosition].name,
                        bookmarkList[bindingAdapterPosition].model,
                        bookmarkList[bindingAdapterPosition].csc
                )
            }
            delete.setOnClickListener { onClickListener.onDeleteClicked(bookmarkList[bindingAdapterPosition].device) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_bookmark_items, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = bookmarkList[position].name
        holder.model.text = bookmarkList[position].model
        holder.csc.text = bookmarkList[position].csc
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }

    internal fun setBookmarks(bookmarkList: List<BookmarkEntity>) {
        this.bookmarkList = bookmarkList
        notifyDataSetChanged()
    }

    interface MyAdapterListener {
        fun onLayoutClicked(model: String, csc: String)
        fun onEditClicked(id: Long, name: String, model: String, csc: String)
        fun onDeleteClicked(device: String)
    }
}