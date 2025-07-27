package com.illusion.checkfirm.features.catcher.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.data.model.InfoCatcherEntity
import com.illusion.checkfirm.databinding.RowInfoCatcherItemsBinding

class InfoCatcherAdapter(
    private var deviceList: List<InfoCatcherEntity>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<InfoCatcherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoCatcherViewHolder {
        val binding =
            RowInfoCatcherItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return InfoCatcherViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: InfoCatcherViewHolder, position: Int) {
        holder.bind(deviceList[position])
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    internal fun setDevices(deviceList: List<InfoCatcherEntity>) {
        this.deviceList = deviceList
        notifyDataSetChanged()
    }
}
