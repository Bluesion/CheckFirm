package com.illusion.checkfirm.features.settings.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.databinding.RowSettingsLanguageItemsBinding

class LanguageViewHolder(
    private val binding: RowSettingsLanguageItemsBinding,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Pair<String, String>) {
        binding.layout.setOnClickListener {
            onItemClicked(absoluteAdapterPosition)
        }
        binding.radio.setOnClickListener {
            onItemClicked(absoluteAdapterPosition)
        }

        binding.language.text = item.first
        if (item.second == AppCompatDelegate.getApplicationLocales().toLanguageTags()) {
            binding.radio.isChecked = true
        }
    }
}