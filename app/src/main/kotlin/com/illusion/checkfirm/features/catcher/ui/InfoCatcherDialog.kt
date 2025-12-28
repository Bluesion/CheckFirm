package com.illusion.checkfirm.features.catcher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.google.firebase.messaging.FirebaseMessaging
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.databinding.DialogInfoCatcherBinding
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModelFactory
import com.illusion.checkfirm.features.catcher.viewmodel.InfoCatcherViewModel
import com.illusion.checkfirm.features.catcher.viewmodel.InfoCatcherViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class InfoCatcherDialog : CheckFirmBottomSheetDialogFragment<DialogInfoCatcherBinding>() {

    private val icViewModel by viewModels<InfoCatcherViewModel>()

    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    override fun onCreateView(inflater: LayoutInflater) = DialogInfoCatcherBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookmarkViewModel.bookmarkList.collect {
                    if (it.isEmpty()) {
                        binding!!.chipGroup.visibility = View.GONE
                    } else {
                        for (element in it) {
                            binding!!.chipGroup.removeAllViews()
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
                icViewModel.insert(model, csc)
                FirebaseMessaging.getInstance().subscribeToTopic(model + csc)
                dismiss()
            } else {
                Toast.makeText(
                    requireContext(), getString(R.string.check_device), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
