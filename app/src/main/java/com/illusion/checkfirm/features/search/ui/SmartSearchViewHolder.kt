package com.illusion.checkfirm.features.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.databinding.RowMainSearchDialogSmartSearchItemsBinding

class SmartSearchViewHolder(
    private val binding: RowMainSearchDialogSmartSearchItemsBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(title: String, value: String, description: String) {
        binding.smartSearchTitle.text = title
        binding.smartSearchValue.text = value
        binding.smartSearchDescription.text = description
    }
}