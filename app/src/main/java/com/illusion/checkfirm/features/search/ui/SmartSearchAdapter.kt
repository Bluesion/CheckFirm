package com.illusion.checkfirm.features.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.databinding.RowMainSearchDialogSmartSearchItemsBinding

class SmartSearchAdapter(
    private val titleList: Array<String>,
    private val valueList: Array<String>,
    private val descriptionList: Array<String>
) : RecyclerView.Adapter<SmartSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartSearchViewHolder {
        val binding = RowMainSearchDialogSmartSearchItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return SmartSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmartSearchViewHolder, position: Int) {
        holder.bind(titleList[position], valueList[position], descriptionList[position])
    }

    override fun getItemCount(): Int {
        return titleList.size
    }
}
