package com.illusion.checkfirm.main

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.RowMultiSearchItemsBinding

class MultiAdapter(val context: Context, private val isOfficial: Boolean, private val firebase: Boolean,
                   private val total: Int, val onClickListener: MyAdapterListener) : RecyclerView.Adapter<MultiAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: RowMultiSearchItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val card = binding.card
        var image = binding.image
        var model = binding.model
        var title = binding.title
        var text = binding.text

        var detailLayout = binding.detailLayout

        var dateLayout = binding.dateLayout
        var date = binding.smartSearchDate

        var discovererLayout = binding.discovererLayout
        var discoverer = binding.smartSearchDiscoverer

        var androidVersion = binding.smartSearchAndroidVersion

        init {
            card.setOnClickListener { v -> onClickListener.onLayoutClicked(v, bindingAdapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowMultiSearchItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val colors = listOf("#8BC34A", "#FFC107", "#2196F3", "#673AB7")
        holder.image.setColorFilter(Color.parseColor(colors[position]), PorterDuff.Mode.SRC_IN)

        val model = CheckFirm.searchModel[position]
        val csc = CheckFirm.searchCSC[position]
        holder.model.text = String.format(context.getString(R.string.device_format), model, csc)

        if (isOfficial) {
            holder.title.text = context.getString(R.string.official_latest)
            holder.text.text = CheckFirm.searchResult[position].officialLatestFirmware
        } else {
            val testLatest = CheckFirm.searchResult[position].testLatestFirmware
            val testDecrypted = CheckFirm.searchResult[position].testDecrypted

            holder.title.text = context.getString(R.string.test_latest)
            if (testDecrypted == "null") {
                holder.text.text = testLatest
            } else {
                holder.text.text = testDecrypted
            }

            if (testLatest != context.getString(R.string.search_error)) {
                holder.detailLayout.visibility = View.VISIBLE
                holder.androidVersion.text = CheckFirm.searchResult[position].testAndroidVersion

                if (!firebase) {
                    holder.dateLayout.visibility = View.VISIBLE
                    holder.date.text = CheckFirm.searchResult[position].testDiscoveryDate
                    holder.discovererLayout.visibility = View.VISIBLE
                    holder.discoverer.text = CheckFirm.searchResult[position].testDiscoverer
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return total + 1
    }

    interface MyAdapterListener {
        fun onLayoutClicked(v: View, position: Int)
    }
}
