package com.illusion.checkfirm.features.search.ui

import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.databinding.RowMainSearchDialogFirmwareItemsBinding

class SearchDialogViewHolder(
    private val binding: RowMainSearchDialogFirmwareItemsBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        binding.data.text = item
    }
}