package com.illusion.checkfirm.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.catcher.InfoCatcherEntity

class InfoCatcherAdapter(private val context: Context, private var deviceList: List<InfoCatcherEntity>,
                         val onClickListener: MyAdapterListener): RecyclerView.Adapter<InfoCatcherAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val delete: AppCompatImageButton = view.findViewById(R.id.delete)
        var device: MaterialTextView = view.findViewById(R.id.device)

        init {
            delete.setOnClickListener { onClickListener.onDeleteClicked(deviceList[adapterPosition].device) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_search_items, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.device.text = String.format(context.getString(R.string.device_format), deviceList[position].model, deviceList[position].csc)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    internal fun setDevices(deviceList: List<InfoCatcherEntity>) {
        this.deviceList = deviceList
        notifyDataSetChanged()
    }

    interface MyAdapterListener {
        fun onDeleteClicked(device: String)
    }
}