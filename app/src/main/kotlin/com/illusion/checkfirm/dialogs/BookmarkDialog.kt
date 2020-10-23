package com.illusion.checkfirm.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import com.illusion.checkfirm.databinding.DialogBookmarkBinding
import java.util.*

class BookmarkDialog(private val shouldUpdate: Boolean, private val id: Long,
                     private val name: String, private val model: String, private val csc: String) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogBookmarkBinding.inflate(inflater)

        binding.model.setSelection(binding.model.text!!.length)

        val viewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)

        if (shouldUpdate) {
            binding.title.text = getString(R.string.bookmark_edit)
        } else {
            binding.title.text = getString(R.string.bookmark_new)
        }

        if (name.isNotBlank()) {
            binding.name.setText(name)
        }

        if (model.isNotBlank()) {
            binding.model.setText(model)
        }

        if (csc.isNotBlank()) {
            binding.csc.setText(csc)
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.save.setOnClickListener {
            val name = binding.name.text.toString()
            val model = binding.model.text!!.trim().toString().toUpperCase(Locale.US)
            val csc = binding.csc.text!!.trim().toString().toUpperCase(Locale.US)

            if (shouldUpdate) {
                viewModel.update(name, id, model, csc, "")
            } else {
                viewModel.insert(name, model, csc, "")
            }
            dismiss()
        }

        return binding.root
    }
}
