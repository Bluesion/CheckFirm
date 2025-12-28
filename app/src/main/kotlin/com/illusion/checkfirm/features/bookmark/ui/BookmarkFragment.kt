package com.illusion.checkfirm.features.bookmark.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.common.ui.base.CheckFirmFragment
import com.illusion.checkfirm.common.ui.recyclerview.RecyclerViewVerticalMarginDecorator
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.databinding.FragmentBookmarkBinding
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : CheckFirmFragment<FragmentBookmarkBinding>() {

    private val bookmarkViewModel by activityViewModels<BookmarkViewModel>()
    private val categoryViewModel by activityViewModels<CategoryViewModel>()
    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onCreateView(inflater: LayoutInflater) = FragmentBookmarkBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    bookmarkViewModel.updateCategory(
                        Tools.getCategory(
                            requireContext(), categoryViewModel.currentCategoryList.value[position]
                        )
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        bookmarkAdapter = BookmarkAdapter(
            bookmarkList = listOf(),
            onLayoutClicked = { model, csc ->
                (requireActivity() as BookmarkCategoryActivity).onBookmarkCardClicked(model, csc)
            },
            onEditClicked = { bookmarkEntity ->
                BookmarkDialog(
                    bookmarkEntity,
                    onDialogClose = { entity ->
                        bookmarkViewModel.addOrEditBookmark(entity)
                    }
                ).show(requireActivity().supportFragmentManager, null)
            },
            onDeleteClicked = { device ->
                bookmarkViewModel.deleteBookmark(device)
            }
        )
        binding!!.recyclerView.adapter = bookmarkAdapter

        binding!!.recyclerView.addItemDecoration(
            RecyclerViewVerticalMarginDecorator(
                Tools.dpToPx(
                    requireContext(), 8f
                )
            )
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.currentCategoryList.collect {
                    if (it.size == 1) {
                        binding!!.categorySpinner.visibility = View.GONE
                    } else {
                        binding!!.categorySpinner.setList(it)
                        binding!!.categorySpinner.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookmarkViewModel.bookmarkList.collect {
                    bookmarkAdapter.setBookmarks(it)
                }
            }
        }
    }
}