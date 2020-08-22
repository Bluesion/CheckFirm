package com.illusion.checkfirm.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.DialogSherlockBinding

class SherlockDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogSherlockBinding.inflate(inflater)

        binding.samsungEncryptedText.text = requireArguments().getString("original_text")
        binding.userEncryptedText.text = requireArguments().getString("user_text")

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

    companion object {
        fun newInstance(originalText: String, userText: String): SherlockDialog {
            val f = SherlockDialog()

            val args = Bundle()
            args.putString("original_text", originalText)
            args.putString("user_text", userText)
            f.arguments = args

            return f
        }
    }
}
