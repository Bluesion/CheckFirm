package com.illusion.checkfirm.features.search.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmFragment
import com.illusion.checkfirm.common.ui.recyclerview.RecyclerViewVerticalMarginDecorator
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.model.local.DeviceItem
import com.illusion.checkfirm.data.model.local.SearchDeviceItem
import com.illusion.checkfirm.databinding.FragmentSearchTabBookmarkBinding
import com.illusion.checkfirm.features.bookmark.ui.BookmarkCategoryActivity
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModelFactory
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModelFactory
import com.illusion.checkfirm.features.search.util.SearchValidationResult
import com.illusion.checkfirm.features.search.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TabBookmarkFragment : CheckFirmFragment<FragmentSearchTabBookmarkBinding>() {

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val bookmarkViewModel by viewModels<BookmarkViewModel> {
        BookmarkViewModelFactory(
            (requireActivity().application as CheckFirm).repositoryProvider.getBCRepository(),
            (requireActivity().application as CheckFirm).repositoryProvider.getSettingsRepository()
        )
    }

    private val categoryViewModel by viewModels<CategoryViewModel> {
        CategoryViewModelFactory(
            getString(R.string.category_all),
            (requireActivity().application as CheckFirm).repositoryProvider.getBCRepository()
        )
    }

    private lateinit var searchBookmarkAdapter: SearchDeviceListAdapter

    override fun onCreateView(inflater: LayoutInflater) =
        FragmentSearchTabBookmarkBinding.inflate(inflater)

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

        searchBookmarkAdapter = SearchDeviceListAdapter(
            onItemClicked = {
                when (searchViewModel.onItemClicked(it.device)) {
                    SearchValidationResult.INVALID_DEVICE -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.check_device),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.multi_search_limit),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        // Don't have to show a toast message for SearchValidationResult.SUCCESS and SearchValidationResult.DUPLICATED_DEVICE.
                    }
                }
            },
            onDeleteClicked = {
                searchViewModel.removeFromSearchList(
                    searchBookmarkAdapter.currentList[it].device
                )
            }
        )

        binding!!.recyclerView.apply {
            adapter = searchBookmarkAdapter
            addItemDecoration(
                RecyclerViewVerticalMarginDecorator(
                    Tools.dpToPx(
                        requireContext(), 8f
                    )
                )
            )
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.searchList.collect { items ->
                val searchList = items.map { it.device }
                searchBookmarkAdapter.currentList.forEachIndexed { index, item ->
                    searchBookmarkAdapter.currentList[index].isChecked = item.device in searchList
                    searchBookmarkAdapter.notifyDataSetChanged()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.currentCategoryList.collectLatest {
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
                bookmarkViewModel.bookmarkList.collectLatest {
                    if (it.isEmpty()) {
                        binding!!.recyclerView.visibility = View.GONE
                        binding!!.emptyText.visibility = View.VISIBLE
                        binding!!.suggestionSearchMyDevice.visibility = View.VISIBLE
                        binding!!.suggestionAddBookmark.visibility = View.VISIBLE
                    } else {
                        val tempList = mutableListOf<SearchDeviceItem>()
                        for (bookmark in it) {
                            tempList.add(
                                SearchDeviceItem(
                                    device = DeviceItem(
                                        bookmark.model,
                                        bookmark.csc
                                    ),
                                    additionalInfo = bookmark.name,
                                    isDeleteButtonVisible = false,
                                    isChecked = DeviceItem(
                                        bookmark.model,
                                        bookmark.csc
                                    ) in searchViewModel.searchList.value.map { it.device }
                                )
                            )
                        }
                        searchBookmarkAdapter.submitList(tempList)

                        binding!!.emptyText.visibility = View.GONE
                        binding!!.suggestionSearchMyDevice.visibility = View.GONE
                        binding!!.suggestionAddBookmark.visibility = View.GONE
                        binding!!.recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding!!.suggestionSearchMyDevice.setOnClickListener {
            when (searchViewModel.addToSearchList(DeviceItem(Build.MODEL, Tools.getCSC()))) {
                SearchValidationResult.INVALID_DEVICE -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.check_device),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.multi_search_limit),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    // Don't have to show a toast message for SearchValidationResult.SUCCESS and SearchValidationResult.DUPLICATED_DEVICE.
                }
            }
        }

        binding!!.suggestionAddBookmark.setOnClickListener {
            Intent(requireContext(), BookmarkCategoryActivity::class.java).apply {
                startActivity(this)
            }
        }
    }
}
