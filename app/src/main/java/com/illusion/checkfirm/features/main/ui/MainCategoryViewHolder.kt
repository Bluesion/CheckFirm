package com.illusion.checkfirm.features.main.ui

import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.databinding.RowMainCategoryItemsBinding

class MainCategoryViewHolder(
    private val binding: RowMainCategoryItemsBinding,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(category: String, checkedPosition: Int) {
        binding.layout.setOnClickListener {
            onItemClicked(absoluteAdapterPosition)
        }

        binding.checkBox.setOnClickListener {
            onItemClicked(absoluteAdapterPosition)
        }

        binding.checkBox.isChecked = absoluteAdapterPosition == checkedPosition
        binding.name.text = category
    }
}