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

class SingleAdapter(val context: Context, private val model: String, private val csc: String,
                    private val officialLatest: String, private val testLatest: String, private val isSmart: Boolean,
                    private val date: String, private val downgrade: String, private val type: String,
                    val onClickListener: MyAdapterListener) : RecyclerView.Adapter<SingleAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val officialCard: MaterialCardView = view.findViewById(R.id.official_card)
        private val testCard: MaterialCardView = view.findViewById(R.id.test_card)

        var officialModel: MaterialTextView = view.findViewById(R.id.model_official)
        var officialTitle: MaterialTextView = view.findViewById(R.id.latest_official_firmware_title)
        var officialText: MaterialTextView = view.findViewById(R.id.latest_official_firmware_text)

        var testModel: MaterialTextView = view.findViewById(R.id.model_test)
        var testTitle: MaterialTextView = view.findViewById(R.id.latest_test_firmware_title)
        var testText: MaterialTextView = view.findViewById(R.id.latest_test_firmware_text)

        val detail: LinearLayout = view.findViewById(R.id.detail)
        var date: MaterialTextView = view.findViewById(R.id.smart_search_date)
        var downgrade: MaterialTextView = view.findViewById(R.id.smart_search_downgrade)
        var type: MaterialTextView = view.findViewById(R.id.smart_search_type)

        init {
            officialCard.setOnClickListener { v -> onClickListener.onOfficialCardClicked(v, bindingAdapterPosition) }
            testCard.setOnClickListener { v -> onClickListener.onTestCardClicked(v, bindingAdapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_single_search_items, parent, false)

        return MyViewHolder(itemView)
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