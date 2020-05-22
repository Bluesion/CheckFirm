package com.illusion.checkfirm.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import java.util.*

class BookmarkDialog : BottomSheetDialogFragment() {

    private lateinit var cancelButton: MaterialButton
    private lateinit var saveButton: MaterialButton
    private lateinit var name: TextInputEditText
    private lateinit var model: TextInputEditText
    private lateinit var csc: TextInputEditText
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.dialog_bookmark, container, false)

        cancelButton = rootView.findViewById(R.id.cancel)
        saveButton = rootView.findViewById(R.id.save)

        name = rootView.findViewById(R.id.name)
        model = rootView.findViewById(R.id.model)
        model.setSelection(model.text!!.length)
        csc = rootView.findViewById(R.id.csc)

        viewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)

        val shouldUpdate = requireArguments().getBoolean("shouldUpdate")
        val bundleId = requireArguments().getLong("id")
        val bundleName = requireArguments().getString("name")
        val bundleModel = requireArguments().getString("model")
        val bundleCSC = requireArguments().getString("csc")

        val title = rootView.findViewById<MaterialTextView>(R.id.title)
        if (shouldUpdate) {
            title.text = getString(R.string.edit_bookmark)
            if (bundleName != "") {
                name.setText(bundleName)
            }

            if (bundleModel != "") {
                model.setText(bundleModel)
            }

            if (bundleCSC != "") {
                csc.setText(bundleCSC)
            }
        } else {
            title.text = getString(R.string.new_bookmark)
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        saveButton.setOnClickListener {
            val name = name.text.toString()
            val model = model.text!!.trim().toString().toUpperCase(Locale.US)
            val csc = csc.text!!.trim().toString().toUpperCase(Locale.US)

            if (shouldUpdate) {
                viewModel.update(name, bundleId, model, csc, "")
            } else {
                viewModel.insert(name, model, csc, "")
            }
            dismiss()
        }

        return rootView
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