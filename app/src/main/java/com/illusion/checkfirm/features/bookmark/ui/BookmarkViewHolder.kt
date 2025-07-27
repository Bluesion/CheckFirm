package com.illusion.checkfirm.features.bookmark.ui

import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.data.model.BookmarkEntity
import com.illusion.checkfirm.databinding.RowBookmarkItemsBinding

class BookmarkViewHolder(
    private val binding: RowBookmarkItemsBinding,
    private val onLayoutClicked: (model: String, csc: String) -> Unit,
    private val onEditClicked: (BookmarkEntity) -> Unit,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BookmarkEntity) {
        binding.bookmarkLayout.setOnClickListener {
            onLayoutClicked(item.model, item.csc)
        }

        binding.edit.setOnClickListener {
            onEditClicked(item)
        }

        binding.delete.setOnClickListener {
            onDeleteClicked(item.device)
        }

        binding.name.text = item.name
        binding.model.text = item.model
        binding.csc.text = item.csc
    }
}