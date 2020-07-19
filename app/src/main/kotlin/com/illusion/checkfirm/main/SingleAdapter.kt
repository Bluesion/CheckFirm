package com.illusion.checkfirm.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.RowSingleSearchItemsBinding

class SingleAdapter(val context: Context, private val model: String, private val csc: String,
                    private val officialLatest: String, private val testLatest: String, private val isSmart: Boolean,
                    private val date: String, private val downgrade: String, private val type: String,
                    val onClickListener: MyAdapterListener) : RecyclerView.Adapter<SingleAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: RowSingleSearchItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val officialCard: MaterialCardView = binding.officialCard
        private val testCard: MaterialCardView = binding.testCard

        var officialModel: MaterialTextView = binding.modelOfficial
        var officialTitle: MaterialTextView = binding.latestOfficialFirmwareTitle
        var officialText: MaterialTextView = binding.latestOfficialFirmwareText

        var testModel: MaterialTextView = binding.modelTest
        var testTitle: MaterialTextView = binding.latestTestFirmwareTitle
        var testText: MaterialTextView = binding.latestTestFirmwareText

        val detail: LinearLayout = binding.detail
        var date: MaterialTextView = binding.smartSearchDate
        var downgrade: MaterialTextView = binding.smartSearchDowngrade
        var type: MaterialTextView = binding.smartSearchType

        init {
            officialCard.setOnClickListener { v -> onClickListener.onOfficialCardClicked(v, bindingAdapterPosition) }
            testCard.setOnClickListener { v -> onClickListener.onTestCardClicked(v, bindingAdapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowSingleSearchItemsBinding.inflate(LayoutInflater.from(parent.context))

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val device = String.format(context.getString(R.string.device_format), model, csc)
        holder.officialModel.text = device
        holder.testModel.text = device

        holder.officialTitle.text = context.getString(R.string.latest_official)
        holder.officialText.text = officialLatest

        holder.testTitle.text = context.getString(R.string.latest_test)
        holder.testText.text = testLatest

        if (isSmart) {
            holder.detail.visibility = View.VISIBLE
            holder.date.text = date
            holder.downgrade.text = downgrade
            holder.type.text = type
        } else {
            holder.detail.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    interface MyAdapterListener {
        fun onOfficialCardClicked(v: View, position: Int)
        fun onTestCardClicked(v: View, position: Int)
    }
}