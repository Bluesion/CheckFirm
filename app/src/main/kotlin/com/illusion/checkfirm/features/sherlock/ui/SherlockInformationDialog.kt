package com.illusion.checkfirm.features.sherlock.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogSherlockInformationBinding

class SherlockInformationDialog(private val isManualDialog: Boolean) :
    CheckFirmBottomSheetDialogFragment<DialogSherlockInformationBinding>() {

    override fun onCreateView(inflater: LayoutInflater) =
        DialogSherlockInformationBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isManualDialog) {
            binding!!.title.text = getString(R.string.sherlock)
            binding!!.message.text = getString(R.string.sherlock_manual_description)
        } else {
            binding!!.title.text = getString(R.string.sherlock_script)
            binding!!.message.text = getString(R.string.sherlock_script_description)
        }

        binding!!.close.setOnClickListener {
            dismiss()
        }
    }
}
