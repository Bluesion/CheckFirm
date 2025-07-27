package com.illusion.checkfirm.features.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogCategoryBinding
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch

class CategoryDialog(
    private val category: String,
    private val onCategoryClicked: (String) -> Unit
) : CheckFirmBottomSheetDialogFragment<DialogCategoryBinding>() {

    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater) = DialogCategoryBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNotDraggable()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.currentCategoryList.collect { categoryList ->
                    var checkedPosition = 0
                    if (categoryList.size != 1) {
                        for ((index, value) in categoryList.withIndex()) {
                            if (category == value) {
                                checkedPosition = index
                                break
                            }
                        }
                    }

                    binding!!.recyclerView.adapter =
                        CategoryAdapter(
                            categoryList = categoryList,
                            checkedPosition = checkedPosition,
                            onItemClicked = {
                                onCategoryClicked(Tools.getCategory(requireContext(), categoryList[it]))
                                dismiss()
                            }
                        )
                }
            }
        }

        binding!!.ok.setOnClickListener {
            dismiss()
        }
    }
}
