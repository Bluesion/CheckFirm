package com.illusion.checkfirm.features.search.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.databinding.ActivitySearchBinding
import com.illusion.checkfirm.features.search.viewmodel.HistoryViewModel
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.model.DeviceItem
import com.illusion.checkfirm.data.model.SearchDeviceItem
import com.illusion.checkfirm.features.search.util.SearchValidationResult
import com.illusion.checkfirm.features.search.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchActivity : CheckFirmActivity<ActivitySearchBinding>() {

    private lateinit var searchListAdapter: SearchDeviceListAdapter
    private val searchViewModel: SearchViewModel by viewModels()

    private val historyViewModel: HistoryViewModel by viewModels()

    override fun createBinding() = ActivitySearchBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setVerticalInset(binding.layouts)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initTopBar()
        initBottomSheet()
        initAdapter()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchList.collectLatest {
                    binding.summary.text = resources.getQuantityString(
                        R.plurals.search_device_summary, searchViewModel.searchList.value.size, searchViewModel.searchList.value.size
                    )
                    searchListAdapter.submitList(it.toList())
                }
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, TabBookmarkFragment()).commit()
    }

    private fun initTopBar() {
        binding.back.setOnClickListener {
            finish()
        }

        binding.add.setOnClickListener {
            when (searchViewModel.addToSearchList(binding.model.text.toString(), binding.csc.text.toString())) {
                SearchValidationResult.INVALID_DEVICE -> {
                    Toast.makeText(
                        this,
                        getString(R.string.check_device),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED -> {
                    Toast.makeText(
                        this,
                        getString(R.string.multi_search_limit),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    // Don't have to show a toast message for SearchValidationResult.SUCCESS and SearchValidationResult.DUPLICATED_DEVICE.
                }
            }
        }

        binding.model.setSelection(binding.model.text!!.length)
        binding.csc.setOnEditorActionListener { _, i, _ ->
            when (i) {
                EditorInfo.IME_ACTION_DONE -> {
                    binding.add.performClick()
                    true
                }

                else -> true
            }
        }

        binding.bookmarkTab.setOnClickListener {
            binding.bookmarkTab.setTabSelected(true)
            binding.recentTab.setTabSelected(false)
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, TabBookmarkFragment()).commit()
        }

        binding.recentTab.setOnClickListener {
            binding.bookmarkTab.setTabSelected(false)
            binding.recentTab.setTabSelected(true)
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id, TabHistoryFragment()).commit()
        }
    }

    private fun initBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.controllerLayout)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.search.setOnClickListener {
            search()
        }
    }

    private fun initAdapter() {
        searchListAdapter =
            SearchDeviceListAdapter(
                onItemClicked = {},
                onDeleteClicked = { searchViewModel.removeFromSearchList(it) }
            )
        binding.searchDeviceRecyclerView.apply {
            adapter = searchListAdapter
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }

    private fun search() {
        if (searchViewModel.searchList.value.isEmpty()) {
            // '추가' 버튼을 클릭하지 않고, Model EditText와 CSC EditText에 값을 입력하고 바로 검색을 누른 경우
            if (Tools.isValidDevice(
                    binding.model.text.toString().trim(),
                    binding.csc.text.toString().trim()
                )
            ) {
                if (Tools.isOnline(this)) {
                    historyViewModel.createHistory(
                        listOf(
                            SearchDeviceItem(
                                device = DeviceItem(
                                    binding.model.text.toString().trim(),
                                    binding.csc.text.toString().trim()
                                )
                            )
                        )
                    )
                    intent.putExtra("model", binding.model.text.toString().trim())
                    intent.putExtra("csc", binding.csc.text.toString().trim())
                    intent.putExtra("total", 1)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(
                        this, R.string.check_network, Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, getString(R.string.search_list_empty), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            var modelString = ""
            var cscString = ""

            if (Tools.isOnline(this)) {
                for (element in searchViewModel.searchList.value) {
                    modelString += element.device.model + "%"
                    cscString += element.device.csc + "%"
                }

                historyViewModel.createHistory(searchViewModel.searchList.value)

                intent.putExtra("model", modelString)
                intent.putExtra("csc", cscString)
                intent.putExtra("total", searchViewModel.searchList.value.size)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(
                    this, R.string.check_network, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}