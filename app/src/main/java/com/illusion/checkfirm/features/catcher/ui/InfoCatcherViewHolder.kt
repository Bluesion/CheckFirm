package com.illusion.checkfirm.features.catcher.ui

import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.data.model.InfoCatcherEntity
import com.illusion.checkfirm.databinding.RowInfoCatcherItemsBinding

class InfoCatcherViewHolder(
    private val binding: RowInfoCatcherItemsBinding,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: InfoCatcherEntity) {
        binding.delete.setOnClickListener {
            onItemClicked(item.device)
        }

        binding.device.text = String.format(
            binding.device.context.getString(R.string.device_format_1), item.model, item.csc
        )
    }
}