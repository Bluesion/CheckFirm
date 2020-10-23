package com.illusion.checkfirm.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.RowSingleSearchItemsBinding

class SingleAdapter(val context: Context, private val firebase: Boolean,
                    val onClickListener: MyAdapterListener) : RecyclerView.Adapter<SingleAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: RowSingleSearchItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val officialCard = binding.officialCard
        private val testCard = binding.testCard

        var officialModel = binding.modelOfficial
        var officialTitle = binding.latestOfficialFirmwareTitle
        var officialText = binding.latestOfficialFirmwareText

        var testModel = binding.modelTest
        var testTitle = binding.latestTestFirmwareTitle
        var testText = binding.latestTestFirmwareText

        var detailLayout = binding.detailLayout

        var dateLayout = binding.dateLayout
        var date = binding.smartSearchDate

        var discovererLayout = binding.discovererLayout
        var discoverer = binding.smartSearchDiscoverer

        var androidVersion = binding.smartSearchAndroidVersion

        init {
            officialCard.setOnClickListener { v -> onClickListener.onOfficialCardClicked(v, bindingAdapterPosition) }
            testCard.setOnClickListener { v -> onClickListener.onTestCardClicked(v, bindingAdapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowSingleSearchItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val searchPrefs = context.getSharedPreferences("search", Context.MODE_PRIVATE)

        val model = searchPrefs.getString("search_model_0", "null")!!
        val csc = searchPrefs.getString("search_csc_0", "null")!!
        val device = String.format(context.getString(R.string.device_format), model, csc)
        holder.officialModel.text = device
        holder.testModel.text = device

        holder.officialTitle.text = context.getString(R.string.official_latest)
        holder.officialText.text = searchPrefs.getString("official_latest_0", "null")!!

        val testLatest = searchPrefs.getString("test_latest_0", "null")!!
        val testDecrypted = searchPrefs.getString("test_decrypted_0", "null")!!
        holder.testTitle.text = context.getString(R.string.test_latest)

        if (testDecrypted == "null") {
            holder.testText.text = testLatest
        } else {
            holder.testText.text = testDecrypted
        }

        if (testLatest != context.getString(R.string.search_error)) {
            holder.detailLayout.visibility = View.VISIBLE
            holder.androidVersion.text = searchPrefs.getString("test_latest_android_version_0", "")!!

            if (!firebase) {
                holder.dateLayout.visibility = View.VISIBLE
                holder.date.text = searchPrefs.getString("test_discovery_date_0", "")!!
                holder.discovererLayout.visibility = View.VISIBLE
                holder.discoverer.text = searchPrefs.getString("test_discoverer_0", "Unknown")!!
            }
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
