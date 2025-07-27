package com.illusion.checkfirm.features.bookmark.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.data.model.CategoryDeviceListItem
import com.illusion.checkfirm.databinding.RowCategoryDeviceItemBinding

class CategoryDeviceAdapter :
    RecyclerView.Adapter<CategoryDeviceViewHolder>() {

    private var deviceList = ArrayList<CategoryDeviceListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryDeviceViewHolder {
        val binding =
            RowCategoryDeviceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CategoryDeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryDeviceViewHolder, position: Int) {
        holder.bind(deviceList[position])
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    fun checkAll() {
        for (i in deviceList.indices) {
            deviceList[i].isChecked = true
        }
        notifyDataSetChanged()
    }

    fun uncheckAll() {
        for (i in deviceList.indices) {
            deviceList[i].isChecked = false
        }
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<CategoryDeviceListItem> {
        return this.deviceList
    }

    fun setList(deviceList: ArrayList<CategoryDeviceListItem>) {
        this.deviceList = deviceList
        notifyDataSetChanged()
    }
}
