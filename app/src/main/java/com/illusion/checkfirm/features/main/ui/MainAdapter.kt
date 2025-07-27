package com.illusion.checkfirm.features.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.databinding.RowMainSearchResultItemsBinding

class MainAdapter(
    private var total: Int,
    private val isFirebaseEnabled: Boolean,
    private val onCardClicked: (isOfficialCard: Boolean, position: Int) -> Unit,
    private val onCardLongClicked: (firmware: String) -> Unit
) : RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = RowMainSearchResultItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return MainViewHolder(binding, isFirebaseEnabled, onCardClicked, onCardLongClicked)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return total
    }

    fun updateTotal(total: Int) {
        this.total = total
        notifyDataSetChanged()
    }
}