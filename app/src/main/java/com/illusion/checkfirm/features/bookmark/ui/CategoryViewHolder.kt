package com.illusion.checkfirm.features.bookmark.ui

import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.data.model.CategoryEntity
import com.illusion.checkfirm.databinding.RowCategoryItemsBinding

class CategoryViewHolder(
    private val binding: RowCategoryItemsBinding,
    private val onLayoutClicked: (id: Long, name: String, position: Int) -> Unit,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CategoryEntity) {
        binding.categoryLayout.setOnClickListener {
            onLayoutClicked(
                item.id!!, item.name, absoluteAdapterPosition
            )
        }

        binding.delete.setOnClickListener {
            onDeleteClicked(item.name)
        }

        binding.name.text = item.name
    }
}