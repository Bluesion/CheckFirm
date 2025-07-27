package com.illusion.checkfirm.features.search.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.data.model.SearchDeviceItem
import com.illusion.checkfirm.databinding.RowSearchDeviceItemsBinding
import java.util.Locale

class SearchDeviceListViewHolder(
    private val binding: RowSearchDeviceItemsBinding,
    private val onItemClicked: (SearchDeviceItem) -> Unit,
    private val onDeleteClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchDeviceItem) {
        binding.layout.setOnClickListener {
            onItemClicked(item)
        }

        binding.delete.setOnClickListener {
            onDeleteClicked(absoluteAdapterPosition)
        }

        if (item.additionalInfo.isBlank()) {
            binding.checkBox.visibility = View.GONE
        } else {
            binding.checkBox.isChecked = item.isChecked
            binding.checkBox.setOnClickListener {
                binding.layout.performClick()
            }
        }

        if (item.isDeleteButtonVisible) {
            binding.mainText.text = String.format(
                Locale.US,
                binding.subText.context.getString(R.string.device_format_2),
                item.device.model,
                item.device.csc
            )
            binding.subText.text = item.additionalInfo
        } else {
            // 북마크 리스트일 때
            binding.mainText.text = item.additionalInfo
            binding.subText.text = String.format(
                Locale.US,
                binding.subText.context.getString(R.string.device_format_2),
                item.device.model,
                item.device.csc
            )
        }

        if (!item.isDeleteButtonVisible) {
            binding.delete.visibility = View.GONE
        }
    }
}