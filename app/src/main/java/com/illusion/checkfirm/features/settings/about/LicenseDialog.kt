package com.illusion.checkfirm.features.settings.about

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogLicenseBinding
import androidx.core.net.toUri

class LicenseDialog : CheckFirmBottomSheetDialogFragment<DialogLicenseBinding>() {

    override fun onCreateView(inflater: LayoutInflater) = DialogLicenseBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.apache.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, "https://opensource.org/license/apache-2-0".toUri()
                )
            )
        }

        binding!!.mit.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, "https://opensource.org/licenses/MIT".toUri()
                )
            )
        }

        binding!!.ok.setOnClickListener {
            dismiss()
        }
    }
}