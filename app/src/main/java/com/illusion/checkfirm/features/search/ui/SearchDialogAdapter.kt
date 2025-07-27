package com.illusion.checkfirm.features.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.databinding.RowMainSearchDialogFirmwareItemsBinding

class SearchDialogAdapter(
    private val firmwareData: Array<String>
) : RecyclerView.Adapter<SearchDialogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchDialogViewHolder {
        val binding = RowMainSearchDialogFirmwareItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return SearchDialogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchDialogViewHolder, position: Int) {
        holder.bind(firmwareData[position])
    }

    override fun getItemCount(): Int {
        return firmwareData.size
    }
}
