package com.illusion.checkfirm.features.welcome.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.data.model.WelcomeSearchEntity
import com.illusion.checkfirm.databinding.RowWelcomeSearchItemsBinding

class WelcomeSearchAdapter(
    private var deviceList: List<WelcomeSearchEntity>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<WelcomeSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeSearchViewHolder {
        val binding =
            RowWelcomeSearchItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return WelcomeSearchViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: WelcomeSearchViewHolder, position: Int) {
        holder.bind(deviceList[position])
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    fun updateList(newList: List<WelcomeSearchEntity>) {
        deviceList = newList
        notifyDataSetChanged()
    }
}
