package com.illusion.checkfirm.common.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.illusion.checkfirm.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) : Dialog(context) {

    init {
        setCanceledOnTouchOutside(false)
        window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        setContentView(DialogLoadingBinding.inflate(layoutInflater).root)
    }
}