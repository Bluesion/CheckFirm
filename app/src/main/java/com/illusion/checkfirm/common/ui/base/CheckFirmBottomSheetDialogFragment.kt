package com.illusion.checkfirm.common.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.common.util.Tools

abstract class CheckFirmBottomSheetDialogFragment<VB : ViewBinding> : BottomSheetDialogFragment() {

    protected var binding: VB? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = onCreateView(inflater)
        setPadding(binding!!.root)

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    protected abstract fun onCreateView(inflater: LayoutInflater): VB

    protected fun setNotDraggable() {
        (dialog as BottomSheetDialog).behavior.isDraggable = false
    }

    private fun setPadding(view: View) {
        val contentPadding = Tools.dpToPx(requireContext(), 24F)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.setPadding(contentPadding, contentPadding, contentPadding, contentPadding / 3)
            insets
        }
    }
}