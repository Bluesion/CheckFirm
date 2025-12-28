package com.illusion.checkfirm.features.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmFragment
import com.illusion.checkfirm.common.ui.recyclerview.RecyclerViewVerticalMarginDecorator
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.model.local.DeviceItem
import com.illusion.checkfirm.data.model.local.SearchDeviceItem
import com.illusion.checkfirm.databinding.FragmentSearchTabHistoryBinding
import com.illusion.checkfirm.features.search.util.SearchValidationResult
import com.illusion.checkfirm.features.search.viewmodel.HistoryViewModel
import com.illusion.checkfirm.features.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class TabHistoryFragment : CheckFirmFragment<FragmentSearchTabHistoryBinding>() {

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val historyViewModel by activityViewModels<HistoryViewModel>()
    private lateinit var searchHistoryAdapter: SearchDeviceListAdapter
    private var historyList = mutableListOf<SearchDeviceItem>()

    override fun onCreateView(inflater: LayoutInflater) =
        FragmentSearchTabHistoryBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchHistoryAdapter =
            SearchDeviceListAdapter(
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
                    historyViewModel.delete(
                        historyList[it].device.model,
                        historyList[it].device.csc
                    )
                    historyList.removeAt(it)
                    searchHistoryAdapter.notifyItemRemoved(it)
                    if (searchHistoryAdapter.currentList.isEmpty()) {
                        showEmptyLayout(true)
                    }
                }
            )

        binding!!.recyclerView.apply {
            adapter = searchHistoryAdapter
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
                searchHistoryAdapter.currentList.forEachIndexed { index, item ->
                    searchHistoryAdapter.currentList[index].isChecked = item.device in searchList
                    searchHistoryAdapter.notifyDataSetChanged()
                }
            }
        }

        binding!!.deleteAllButton.setOnClickListener {
            historyViewModel.deleteAll()
            historyList.clear()
            searchHistoryAdapter.submitList(historyList)
            showEmptyLayout(true)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            historyViewModel.getAllHistoryList().apply {
                if (isEmpty()) {
                    showEmptyLayout(true)
                } else {
                    for (history in this) {
                        historyList.add(
                            SearchDeviceItem(
                                device = DeviceItem(history.model, history.csc),
                                additionalInfo = "${history.year}-${
                                    String.format(
                                        Locale.US,
                                        "%02d",
                                        history.month
                                    )
                                }-${String.format(Locale.US, "%02d", history.day)}",
                                isDeleteButtonVisible = true,
                                isChecked = DeviceItem(
                                    history.model,
                                    history.csc
                                ) in searchViewModel.searchList.value.map { it.device }
                            )
                        )
                    }
                    searchHistoryAdapter.submitList(historyList)

                    showEmptyLayout(false)
                }
            }
        }
    }

    // TODO: Translate delete all button in all languages.
    private fun showEmptyLayout(isEmpty: Boolean) {
        if (isEmpty) {
            binding!!.deleteAllButton.visibility = View.GONE
            binding!!.emptyText.visibility = View.VISIBLE
            binding!!.suggestionSearchMyDevice.visibility = View.VISIBLE
            binding!!.recyclerView.visibility = View.GONE
        } else {
            binding!!.deleteAllButton.visibility = View.VISIBLE
            binding!!.emptyText.visibility = View.GONE
            binding!!.suggestionSearchMyDevice.visibility = View.GONE
            binding!!.recyclerView.visibility = View.VISIBLE
        }
    }
}