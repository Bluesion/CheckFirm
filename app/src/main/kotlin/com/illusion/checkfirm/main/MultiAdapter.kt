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
import com.illusion.checkfirm.databinding.RowMultiSearchItemsBinding
import com.illusion.checkfirm.primitive.MainItem
import com.illusion.checkfirm.primitive.SmartSearchItem

class MultiAdapter(val context: Context, private val isOfficial: Boolean, private val isSmart: Boolean,
                   private var itemList: List<MainItem>, private var smartSearchList: List<SmartSearchItem>,
                   val onClickListener: MyAdapterListener) : RecyclerView.Adapter<MultiAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: RowMultiSearchItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val card: MaterialCardView = binding.card
        var image: AppCompatImageView = binding.image
        var model: MaterialTextView = binding.model
        var title: MaterialTextView = binding.title
        val detail: LinearLayout = binding.detail
        var text: MaterialTextView = binding.text
        var date: MaterialTextView = binding.smartSearchDate
        var downgrade: MaterialTextView = binding.smartSearchDowngrade
        var type: MaterialTextView = binding.smartSearchType

        init {
            card.setOnClickListener { v -> onClickListener.onLayoutClicked(v, bindingAdapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowMultiSearchItemsBinding.inflate(LayoutInflater.from(parent.context))

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        val colors = listOf("#8BC34A", "#FFC107", "#2196F3", "#673AB7")

        holder.model.text = String.format(context.getString(R.string.device_format), item.model, item.csc)
        holder.image.setColorFilter(Color.parseColor(colors[position]), PorterDuff.Mode.SRC_IN)
        if (isOfficial) {
            holder.title.text = context.getString(R.string.latest_official)
            holder.text.text = item.officialLatest
        } else {
            holder.title.text = context.getString(R.string.latest_test)
            holder.text.text = item.testLatest
        }

        if (isSmart) {
            val smart = smartSearchList[position]
            holder.detail.visibility = View.VISIBLE
            holder.date.text = smart.date
            holder.downgrade.text = smart.downgrade
            holder.type.text = smart.type
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