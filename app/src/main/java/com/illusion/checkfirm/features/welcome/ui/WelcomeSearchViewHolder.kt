package com.illusion.checkfirm.features.welcome.ui

import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.data.model.WelcomeSearchEntity
import com.illusion.checkfirm.databinding.RowWelcomeSearchItemsBinding

class WelcomeSearchViewHolder(
    private val binding: RowWelcomeSearchItemsBinding,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: WelcomeSearchEntity) {
        binding.delete.setOnClickListener {
            onItemClicked(item.device)
        }

        binding.device.text =
            String.format(binding.device.context.getString(R.string.device_format_1), item.model, item.csc)
    }
}