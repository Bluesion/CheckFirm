package com.illusion.checkfirm.features.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.illusion.checkfirm.data.model.SearchDeviceItem
import com.illusion.checkfirm.databinding.RowSearchDeviceItemsBinding

class SearchDeviceListAdapter(
    private val onItemClicked: (SearchDeviceItem) -> Unit,
    private val onDeleteClicked: (Int) -> Unit
) : ListAdapter<SearchDeviceItem, SearchDeviceListViewHolder>(SearchDeviceItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchDeviceListViewHolder {
        val binding =
            RowSearchDeviceItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchDeviceListViewHolder(binding, onItemClicked, onDeleteClicked)
    }

    override fun onBindViewHolder(holder: SearchDeviceListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
