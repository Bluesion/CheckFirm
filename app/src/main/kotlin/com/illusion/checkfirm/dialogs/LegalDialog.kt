package com.illusion.checkfirm.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogLegalBinding

class LegalDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogLegalBinding.inflate(inflater)

        binding.ok.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}