package com.illusion.checkfirm.main

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R

class MultiAdapter(val context: Context, private val isOfficial: Boolean, private val isSmart: Boolean,
                   private var itemList: List<MainItem>, private var smartList: List<SmartItem>,
                   val onClickListener: MyAdapterListener) : RecyclerView.Adapter<MultiAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val card: MaterialCardView = view.findViewById(R.id.card)
        var image: AppCompatImageView = view.findViewById(R.id.image)
        var model: MaterialTextView = view.findViewById(R.id.model)
        var title: MaterialTextView = view.findViewById(R.id.title)
        val detail: LinearLayout = view.findViewById(R.id.detail)
        var text: MaterialTextView = view.findViewById(R.id.text)
        var date: MaterialTextView = view.findViewById(R.id.smart_search_date)
        var downgrade: MaterialTextView = view.findViewById(R.id.smart_search_downgrade)
        var changelog: MaterialTextView = view.findViewById(R.id.smart_search_changelog)

        init {
            card.setOnClickListener { v -> onClickListener.onLayoutClicked(v, adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_multi_search_items, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        val colors = listOf("#8BC34A", "#FFC107", "#2196F3", "#673AB7")

        holder.model.text = String.format(context.getString(R.string.device_format), item.getModel(), item.getCsc())
        holder.image.setColorFilter(Color.parseColor(colors[position]), PorterDuff.Mode.SRC_IN)
        if (isOfficial) {
            holder.title.text = context.getString(R.string.latest_official)
            holder.text.text = item.getOfficialLatest()
        } else {
            holder.title.text = context.getString(R.string.latest_test)
            holder.text.text = item.getTestLatest()
        }

        if (isSmart) {
            val smart = smartList[position]
            holder.detail.visibility = View.VISIBLE
            holder.date.text = smart.getDate()
            holder.downgrade.text = smart.getDowngrade()
            holder.changelog.text = smart.getChangelog()
        } else {
            holder.detail.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface MyAdapterListener {
        fun onLayoutClicked(v: View, position: Int)
    }
}