package com.illusion.checkfirm.features.bookmark.ui

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.illusion.checkfirm.R
import com.illusion.checkfirm.data.model.local.CategoryDeviceListItem
import com.illusion.checkfirm.databinding.RowCategoryDeviceItemBinding

class CategoryDeviceViewHolder(
    private val binding: RowCategoryDeviceItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CategoryDeviceListItem) {
        binding.checkBox.isChecked = item.isChecked
        binding.name.text = item.bookmark.name
        binding.device.text = String.format(
            binding.device.context.getString(R.string.device_format_2),
            item.bookmark.model, item.bookmark.csc
        )
        binding.checkBox.tag = item
        binding.checkBox.setOnClickListener { view ->
            val cb = view as MaterialCheckBox
            val contact = cb.tag as CategoryDeviceListItem
            contact.isChecked = cb.isChecked
            item.isChecked = cb.isChecked
        }
        binding.layout.setOnClickListener {
            binding.checkBox.performClick()
        }
    }
}