package com.illusion.checkfirm.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.databinding.RowHistoryItemsBinding
import com.illusion.checkfirm.primitive.HistoryItem
import kotlin.math.min

class HistoryAdapter(private val historyList: List<HistoryItem>,
                     val onClickListener: MyAdapterListener) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: RowHistoryItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val delete: AppCompatImageView = binding.delete
        private val touch: ConstraintLayout = binding.touchArea
        var model: MaterialTextView = binding.model
        var csc: MaterialTextView = binding.csc
        var date: MaterialTextView = binding.date

        init {
            touch.setOnClickListener { onClickListener.onItemClicked(bindingAdapterPosition) }
            delete.setOnClickListener { onClickListener.onDeleteClicked(bindingAdapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowHistoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
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