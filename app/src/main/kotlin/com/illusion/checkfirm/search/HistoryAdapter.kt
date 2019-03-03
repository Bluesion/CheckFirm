package com.illusion.checkfirm.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.HistoryDB

class HistoryAdapter(private val historyList: List<HistoryDB>, val onClickListener: MyAdapterListener): RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val delete: ImageView = view.findViewById(R.id.delete)
        private val touch: RelativeLayout = view.findViewById(R.id.touchArea)
        var model: TextView = view.findViewById(R.id.model)
        var csc: TextView = view.findViewById(R.id.csc)
        var date: TextView = view.findViewById(R.id.date)

        init {
            touch.setOnClickListener { v -> onClickListener.onItemClicked(v, adapterPosition) }
            delete.setOnClickListener { v -> onClickListener.onDeleteClicked(v, adapterPosition) }
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
        return Math.min(historyList.size, 10)
    }

    interface MyAdapterListener {
        fun onItemClicked(v: View, position: Int)
        fun onDeleteClicked(v: View, position: Int)
    }
}