package com.illusion.checkfirm.features.settings.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogBookmarkResetBinding
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModelFactory
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.CategoryViewModelFactory

class BookmarkResetDialog : CheckFirmBottomSheetDialogFragment<DialogBookmarkResetBinding>() {

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

    override fun onCreateView(inflater: LayoutInflater) =
        DialogBookmarkResetBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.cancel.setOnClickListener {
            dismiss()
        }

        binding!!.ok.setOnClickListener {
            binding!!.message.visibility = View.GONE
            binding!!.loadingProgress.visibility = View.VISIBLE
            bookmarkViewModel.reset()
            categoryViewModel.reset()
            Toast.makeText(
                requireContext(),
                getString(R.string.settings_bookmark_reset_toast),
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
    }
}
