package com.illusion.checkfirm.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.DialogSherlockBinding

class SherlockDialog(private val originalText: String, private val userText: String) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogSherlockBinding.inflate(inflater)

        binding.samsungEncryptedText.text = originalText
        binding.userEncryptedText.text = userText

        if (binding.samsungEncryptedText.text == binding.userEncryptedText.text) {
            binding.status.text = getString(R.string.sherlock_correct)
            binding.status.setTextColor(resources.getColor(R.color.green, requireActivity().theme))
        } else {
            binding.status.text = getString(R.string.sherlock_not_correct)
            binding.status.setTextColor(resources.getColor(R.color.red, requireActivity().theme))
        }

        binding.ok.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}
