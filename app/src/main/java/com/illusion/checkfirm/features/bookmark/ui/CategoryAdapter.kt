package com.illusion.checkfirm.features.bookmark.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.data.model.local.CategoryEntity
import com.illusion.checkfirm.databinding.RowCategoryItemsBinding

class CategoryAdapter(
    private var categoryList: List<CategoryEntity>,
    private val onLayoutClicked: (id: Long, name: String, position: Int) -> Unit,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            RowCategoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CategoryViewHolder(binding, onLayoutClicked, onDeleteClicked)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setCategory(categoryList: List<CategoryEntity>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }
}
