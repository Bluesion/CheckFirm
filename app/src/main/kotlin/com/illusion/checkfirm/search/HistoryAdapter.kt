package com.illusion.checkfirm.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.primitive.HistoryItem
import kotlin.math.min

class HistoryAdapter(private val historyList: List<HistoryItem>,
                     val onClickListener: MyAdapterListener): RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val delete: AppCompatImageView = view.findViewById(R.id.delete)
        private val touch: ConstraintLayout = view.findViewById(R.id.touchArea)
        var model: MaterialTextView = view.findViewById(R.id.model)
        var csc: MaterialTextView = view.findViewById(R.id.csc)
        var date: MaterialTextView = view.findViewById(R.id.date)

        init {
            touch.setOnClickListener { onClickListener.onItemClicked(bindingAdapterPosition) }
            delete.setOnClickListener { onClickListener.onDeleteClicked(bindingAdapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_history_items, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = historyList[position]

        holder.model.text = history.model
        holder.csc.text = history.csc
        holder.date.text = history.date
    }

    override fun getItemCount(): Int {
        return min(historyList.size, 10)
    }

    interface MyAdapterListener {
        fun onItemClicked(position: Int)
        fun onDeleteClicked(position: Int)
    }
}