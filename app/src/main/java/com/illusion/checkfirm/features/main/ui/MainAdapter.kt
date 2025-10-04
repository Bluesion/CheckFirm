package com.illusion.checkfirm.features.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.data.model.local.SearchResultItem
import com.illusion.checkfirm.databinding.RowMainSearchResultItemsBinding

class MainAdapter(
    private var searchResultList: List<SearchResultItem>,
    private var isFirebaseEnabled: Boolean = true,
    private val onCardClicked: (isOfficialCard: Boolean, searchResult: SearchResultItem) -> Unit,
    private val onCardLongClicked: (firmware: String) -> Unit
) : RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = RowMainSearchResultItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return MainViewHolder(binding, onCardClicked, onCardLongClicked)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(searchResultList[position], isFirebaseEnabled)
    }

    override fun getItemCount(): Int {
        return searchResultList.size
    }

    fun updateLists(searchResultList: List<SearchResultItem>) {
        this.searchResultList = searchResultList
        notifyDataSetChanged()
    }

    fun updateFirebaseStatus(isFirebaseEnabled: Boolean) {
        this.isFirebaseEnabled = isFirebaseEnabled
        notifyDataSetChanged()
    }
}