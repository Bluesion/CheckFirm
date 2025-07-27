package com.illusion.checkfirm.features.settings.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogProfileBinding

class ProfileDialog(
    private val currentName: String,
    private val onNameChanged: (String) -> Unit
) : CheckFirmBottomSheetDialogFragment<DialogProfileBinding>() {

    override fun onCreateView(inflater: LayoutInflater) = DialogProfileBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.name.setText(currentName)
        binding!!.name.setSelection(binding!!.name.getText().length)

        binding!!.save.setOnClickListener {
            val name = binding!!.name.getText()
            if (name.isBlank()) {
                onNameChanged("Unknown")
            } else {
                onNameChanged(name)
            }
            dismiss()
        }

        binding!!.cancel.setOnClickListener {
            dismiss()
        }
    }
}
