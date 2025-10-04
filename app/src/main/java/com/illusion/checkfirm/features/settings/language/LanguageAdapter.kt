package com.illusion.checkfirm.features.settings.language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.databinding.RowSettingsLanguageItemsBinding

class LanguageAdapter(
    private val list: List<Pair<String, String>>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding =
            RowSettingsLanguageItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return LanguageViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
