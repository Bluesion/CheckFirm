package com.illusion.checkfirm.features.welcome.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.repository.WelcomeSearchRepository
import com.illusion.checkfirm.data.source.local.DatabaseProvider
import com.illusion.checkfirm.databinding.DialogWelcomeSearchBinding
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModelFactory
import com.illusion.checkfirm.features.welcome.viewmodel.WelcomeSearchViewModel
import com.illusion.checkfirm.features.welcome.viewmodel.WelcomeSearchViewModelFactory
import kotlinx.coroutines.launch
import java.util.Locale

class WelcomeSearchDialog : CheckFirmBottomSheetDialogFragment<DialogWelcomeSearchBinding>() {

    private val wsViewModel by viewModels<WelcomeSearchViewModel> {
        WelcomeSearchViewModelFactory(
            WelcomeSearchRepository(DatabaseProvider.getWelcomeSearchDao())
        )
    }

    private val bookmarkViewModel by viewModels<BookmarkViewModel> {
        BookmarkViewModelFactory(
            (requireActivity().application as CheckFirm).repositoryProvider.getBCRepository(),
            (requireActivity().application as CheckFirm).repositoryProvider.getSettingsRepository()
        )
    }

    override fun onCreateView(inflater: LayoutInflater) =
        DialogWelcomeSearchBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.model.setSelection(binding!!.model.text!!.length)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookmarkViewModel.bookmarkList.collect {
                    if (it.isEmpty()) {
                        binding!!.chipGroup.visibility = View.GONE
                    } else {
                        binding!!.chipGroup.removeAllViews()
                        for (element in it) {
                            val bookmarkChip = Chip(requireContext())
                            bookmarkChip.text = element.name
                            bookmarkChip.isCheckable = false
                            bookmarkChip.setOnClickListener {
                                binding!!.model.setText(element.model)
                                binding!!.csc.setText(element.csc)
                            }
                            binding!!.chipGroup.addView(bookmarkChip)
                        }
                        binding!!.chipGroup.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding!!.cancel.setOnClickListener {
            dismiss()
        }

        binding!!.add.setOnClickListener {
            val model = binding!!.model.text!!.trim().toString().uppercase(Locale.US)
            val csc = binding!!.csc.text!!.trim().toString().uppercase(Locale.US)

            if (Tools.isValidDevice(model, csc)) {
                wsViewModel.insert(model, csc)
                dismiss()
            } else {
                Toast.makeText(
                    requireContext(), getString(R.string.main_search_error_text), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
