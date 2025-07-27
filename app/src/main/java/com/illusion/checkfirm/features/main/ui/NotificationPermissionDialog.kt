package com.illusion.checkfirm.features.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogNotificationPermissionBinding

class NotificationPermissionDialog(
    private val onPositiveButtonClicked : () -> Unit
) : CheckFirmBottomSheetDialogFragment<DialogNotificationPermissionBinding>() {

    override fun onCreateView(inflater: LayoutInflater) = DialogNotificationPermissionBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNotDraggable()

        binding!!.positiveButton.setOnClickListener {
            onPositiveButtonClicked()
            dismiss()
        }

        binding!!.negativeButton.setOnClickListener {
            dismiss()
        }
    }
}
