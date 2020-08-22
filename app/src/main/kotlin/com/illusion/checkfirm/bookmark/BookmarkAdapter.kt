package com.illusion.checkfirm.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.database.bookmark.BookmarkEntity
import com.illusion.checkfirm.databinding.RowBookmarkItemsBinding

class BookmarkAdapter(private var bookmarkList: List<BookmarkEntity>,
                      val onClickListener: MyAdapterListener) : RecyclerView.Adapter<BookmarkAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: RowBookmarkItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        var name = binding.name
        var model = binding.model
        var csc = binding.csc

        init {
            binding.bookmarkLayout.setOnClickListener {
                onClickListener.onLayoutClicked(
                        bookmarkList[bindingAdapterPosition].model,
                        bookmarkList[bindingAdapterPosition].csc
                )
            }
            binding.edit.setOnClickListener {
                onClickListener.onEditClicked(
                        bookmarkList[bindingAdapterPosition].id!!,
                        bookmarkList[bindingAdapterPosition].name,
                        bookmarkList[bindingAdapterPosition].model,
                        bookmarkList[bindingAdapterPosition].csc
                )
            }
            binding.delete.setOnClickListener { onClickListener.onDeleteClicked(bookmarkList[bindingAdapterPosition].device) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowBookmarkItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
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