package com.illusion.checkfirm.features.bookmark.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.model.local.BookmarkEntity
import com.illusion.checkfirm.databinding.DialogBookmarkBinding
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class BookmarkDialog(
    private var bookmarkEntity: BookmarkEntity? = null,
    private val onDialogClose: (entity: BookmarkEntity) -> Unit
) : CheckFirmBottomSheetDialogFragment<DialogBookmarkBinding>() {

    private val categoryViewModel by viewModels<CategoryViewModel> {
        CategoryViewModelFactory(
            getString(R.string.category_all),
            (requireActivity().application as CheckFirm).repositoryProvider.getBCRepository()
        )
    }

    override fun onCreateView(inflater: LayoutInflater) = DialogBookmarkBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (bookmarkEntity == null) {
            binding!!.title.text = getString(R.string.bookmark_new)
            bookmarkEntity = BookmarkEntity(null, "", "", "", "", "", 0)
        } else {
            binding!!.title.text = getString(R.string.bookmark_edit)
            bookmarkEntity!!.let {
                binding!!.name.setText(it.name)
                binding!!.model.setText(it.model)
                binding!!.csc.setText(it.csc)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val categoryList = categoryViewModel.getAllCategoryList()
            if (categoryList.isEmpty()) {
                withContext(Dispatchers.Main) {
                    binding!!.categoryText.visibility = View.GONE
                    binding!!.categorySpinner.visibility = View.GONE
                }
            } else {
                val currentCategoryList = mutableListOf<String>()
                currentCategoryList.add(getString(R.string.category_all))
                for (category in categoryList) {
                    currentCategoryList.add(category.name)
                }
                binding!!.categorySpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View, position: Int, id: Long
                        ) {
                            bookmarkEntity!!.category = Tools.getCategory(
                                requireContext(), currentCategoryList[position]
                            )
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                        }
                    }

                withContext(Dispatchers.Main) {
                    binding!!.categoryText.visibility = View.VISIBLE
                    binding!!.categorySpinner.visibility = View.VISIBLE
                    binding!!.categorySpinner.setList(currentCategoryList)
                }
            }
        }

        binding!!.cancel.setOnClickListener {
            dismiss()
        }

        binding!!.save.setOnClickListener {
            val name = binding!!.name.text.toString()
            val model = binding!!.model.text!!.trim().toString().uppercase(Locale.US)
            val csc = binding!!.csc.text!!.trim().toString().uppercase(Locale.US)

            if (name.isBlank()) {
                Toast.makeText(context, getString(R.string.bookmark_name_error), Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (Tools.isValidDevice(model, csc)) {
                    bookmarkEntity!!.name = name
                    bookmarkEntity!!.model = model
                    bookmarkEntity!!.csc = csc
                    onDialogClose(bookmarkEntity!!)
                    dismiss()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.check_device),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
