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

class BookmarkDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogBookmarkBinding.inflate(inflater)

        binding.model.setSelection(binding.model.text!!.length)

        val viewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)

        val shouldUpdate = requireArguments().getBoolean("shouldUpdate")
        val bundleId = requireArguments().getLong("id")
        val bundleName = requireArguments().getString("name")
        val bundleModel = requireArguments().getString("model")
        val bundleCSC = requireArguments().getString("csc")

        if (shouldUpdate) {
            binding.title.text = getString(R.string.edit_bookmark)
            if (bundleName != "") {
                binding.name.setText(bundleName)
            }

            if (bundleModel != "") {
                binding.model.setText(bundleModel)
            }

            if (bundleCSC != "") {
                binding.csc.setText(bundleCSC)
            }
        } else {
            binding.title.text = getString(R.string.new_bookmark)
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.save.setOnClickListener {
            val name = binding.name.text.toString()
            val model = binding.model.text!!.trim().toString().toUpperCase(Locale.US)
            val csc = binding.csc.text!!.trim().toString().toUpperCase(Locale.US)

            if (shouldUpdate) {
                viewModel.update(name, bundleId, model, csc, "")
            } else {
                viewModel.insert(name, model, csc, "")
            }
            dismiss()
        }

        return binding.root
    }

    companion object {
        fun newInstance(shouldUpdate: Boolean, id: Long, name: String, model: String, csc: String): BookmarkDialog {
            val f = BookmarkDialog()

            val args = Bundle()
            args.putBoolean("shouldUpdate", shouldUpdate)
            args.putLong("id", id)
            args.putString("name", name)
            args.putString("model", model)
            args.putString("csc", csc)
            f.arguments = args

            return f
        }
    }
}