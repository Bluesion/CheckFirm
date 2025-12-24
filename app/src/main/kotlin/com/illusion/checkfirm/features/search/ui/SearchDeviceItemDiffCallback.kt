package com.illusion.checkfirm.features.search.ui

import androidx.recyclerview.widget.DiffUtil
import com.illusion.checkfirm.data.model.local.SearchDeviceItem

class SearchDeviceItemDiffCallback : DiffUtil.ItemCallback<SearchDeviceItem>() {
    override fun areItemsTheSame(oldItem: SearchDeviceItem, newItem: SearchDeviceItem): Boolean {
        return oldItem.device == newItem.device &&
                oldItem.isChecked == newItem.isChecked
    }

    override fun areContentsTheSame(oldItem: SearchDeviceItem, newItem: SearchDeviceItem): Boolean {
        return oldItem == newItem
    }
}