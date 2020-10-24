package com.illusion.checkfirm.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogProfileBinding

class ProfileDialog : BottomSheetDialogFragment() {

    private lateinit var onDialogCloseListener: OnDialogCloseListener

    interface OnDialogCloseListener {
        fun onDialogClose()
    }

    fun setOnDialogCloseListener(listener: OnDialogCloseListener) {
        onDialogCloseListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogProfileBinding.inflate(inflater)
        val sharedPrefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        binding.name.setText(sharedPrefs.getString("profile_user_name", "Unknown"))
        binding.name.setSelection(binding.name.text!!.length)

        binding.save.setOnClickListener {
            val mEditor = sharedPrefs.edit()
            val name = binding.name.text.toString()
            if (name.isBlank()) {
                mEditor.putString("profile_user_name", "Unknown")
            } else {
                mEditor.putString("profile_user_name", binding.name.text.toString())
            }
            mEditor.apply()
            onDialogCloseListener.onDialogClose()
            dismiss()
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}
