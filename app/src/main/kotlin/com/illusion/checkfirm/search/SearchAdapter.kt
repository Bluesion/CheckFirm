package com.illusion.checkfirm.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.primitive.SearchItem

class SearchAdapter(val context: Context, private var searchList: List<SearchItem>,
                    val onClickListener: MyAdapterListener): RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var device: MaterialTextView = view.findViewById(R.id.device)
        private val delete: AppCompatImageButton = view.findViewById(R.id.delete)

        init {
            delete.setOnClickListener { onClickListener.onDeleteClicked(bindingAdapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_search_items, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = searchList[position]
        holder.device.text = String.format(context.getString(R.string.device_format), data.model, data.csc)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    interface MyAdapterListener {
        fun onDeleteClicked(position: Int)
    }
}