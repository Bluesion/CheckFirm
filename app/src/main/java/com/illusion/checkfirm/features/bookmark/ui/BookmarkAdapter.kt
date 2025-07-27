package com.illusion.checkfirm.features.bookmark.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.data.model.BookmarkEntity
import com.illusion.checkfirm.databinding.RowBookmarkItemsBinding

class BookmarkAdapter(
    private var bookmarkList: List<BookmarkEntity>,
    private val onLayoutClicked: (model: String, csc: String) -> Unit,
    private val onEditClicked: (BookmarkEntity) -> Unit,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.Adapter<BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding =
            RowBookmarkItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BookmarkViewHolder(binding, onLayoutClicked, onEditClicked, onDeleteClicked)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(bookmarkList[position])
    }

    override fun getItemCount(): Int {
        return bookmarkList.size
    }

    internal fun setBookmarks(bookmarkList: List<BookmarkEntity>) {
        this.bookmarkList = bookmarkList
        notifyDataSetChanged()
    }
}
