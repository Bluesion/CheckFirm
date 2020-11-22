package com.illusion.checkfirm.dialogs

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.DialogSherlock2Binding

class Sherlock2Dialog(private val i: Int, private val userText: String) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogSherlock2Binding.inflate(inflater)

        val builder = StringBuilder()
        CheckFirm.searchResult[i].testPreviousFirmware.values.forEach { firmware ->
            builder.append(firmware).append("\n")
        }
        builder.deleteAt(builder.lastIndex)

        val start = builder.indexOf(userText)
        val end = start + userText.length

        val spannableBuilder = SpannableStringBuilder(builder.toString())
        spannableBuilder.setSpan(context?.getColor(R.color.red)?.let { ForegroundColorSpan(it) }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.list.append(spannableBuilder)

        binding.ok.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}
