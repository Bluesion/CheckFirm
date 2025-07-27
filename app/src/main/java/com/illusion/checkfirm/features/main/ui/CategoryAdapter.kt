package com.illusion.checkfirm.features.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.databinding.RowMainCategoryItemsBinding

class CategoryAdapter(
    private var categoryList: List<String>,
    private val checkedPosition: Int = 0,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<MainCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCategoryViewHolder {
        val binding =
            RowMainCategoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainCategoryViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: MainCategoryViewHolder, position: Int) {
        holder.bind(categoryList[position], checkedPosition)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}
