package com.illusion.checkfirm.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R

class WelcomeSearchAdapter(private val context: Context, private val modelList: List<String>, private val cscList: List<String>,
                           val onClickListener: MyAdapterListener): RecyclerView.Adapter<WelcomeSearchAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val delete: AppCompatImageButton = view.findViewById(R.id.delete)
        var device: MaterialTextView = view.findViewById(R.id.device)

        init {
            delete.setOnClickListener { onClickListener.onDeleteClicked(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_search_items, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = modelList[position]
        val csc = cscList[position]

        holder.device.text = String.format(context.getString(R.string.device_format), model, csc)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    interface MyAdapterListener {
        fun onDeleteClicked(position: Int)
    }
}