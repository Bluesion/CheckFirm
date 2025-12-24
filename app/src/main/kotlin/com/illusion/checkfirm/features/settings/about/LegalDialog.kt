package com.illusion.checkfirm.features.settings.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogLegalBinding

class LegalDialog : CheckFirmBottomSheetDialogFragment<DialogLegalBinding>() {

    override fun onCreateView(inflater: LayoutInflater) = DialogLegalBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.ok.setOnClickListener {
            dismiss()
        }
    }
}