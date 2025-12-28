package com.illusion.checkfirm.features.bookmark.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.common.ui.base.CheckFirmFragment
import com.illusion.checkfirm.common.ui.recyclerview.RecyclerViewVerticalMarginDecorator
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.databinding.FragmentCategoryBinding
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : CheckFirmFragment<FragmentCategoryBinding>() {

    private val categoryViewModel by activityViewModels<CategoryViewModel>()

    override fun onCreateView(inflater: LayoutInflater) = FragmentCategoryBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter = CategoryAdapter(
            categoryList = listOf(),
            onLayoutClicked = { id, name, position ->
                startActivity(
                    Intent(requireContext(), CategoryEditActivity::class.java).apply {
                        putExtra("id", id)
                        putExtra("name", name)
                        putExtra("position", position)
                    }
                )
            },
            onDeleteClicked = {
                categoryViewModel.deleteCategory(it)
            }
        )

        binding!!.recyclerView.adapter = categoryAdapter
        binding!!.recyclerView.apply {
            addItemDecoration(
                RecyclerViewVerticalMarginDecorator(
                    Tools.dpToPx(
                        requireContext(), 8f
                    )
                )
            )
            adapter = categoryAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.getAllCategory().collect {
                    categoryAdapter.setCategory(it)
                }
            }
        }
    }
}