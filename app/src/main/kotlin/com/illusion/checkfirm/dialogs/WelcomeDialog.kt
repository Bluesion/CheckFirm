package com.illusion.checkfirm.dialogs

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.illusion.checkfirm.R
import com.illusion.checkfirm.utils.Tools

class WelcomeDialog: BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.dialog_welcome, container, false)

        val model = rootView.findViewById<TextInputEditText>(R.id.model)
        val csc = rootView.findViewById<TextInputEditText>(R.id.csc)
        val modelFilters = model.filters
        val newModelFilters = arrayOfNulls<InputFilter>(modelFilters.size + 1)
        System.arraycopy(modelFilters, 0, newModelFilters, 0, modelFilters.size)
        newModelFilters[modelFilters.size] = InputFilter.AllCaps()
        model.filters = newModelFilters

        val cscFilters = csc.filters
        val newCSCFilters = arrayOfNulls<InputFilter>(cscFilters.size + 1)
        System.arraycopy(cscFilters, 0, newCSCFilters, 0, cscFilters.size)
        newCSCFilters[cscFilters.size] = InputFilter.AllCaps()
        csc.filters = newCSCFilters

        val sharedPrefs = activity!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val mEditor = sharedPrefs.edit()

        val saveButton = rootView.findViewById<MaterialButton>(R.id.save)
        saveButton.setOnClickListener {
            mEditor.putString("welcome_model", model.text.toString())
            mEditor.putString("welcome_csc", csc.text.toString())
            mEditor.putBoolean("welcome", true)
            mEditor.apply()
            dismiss()
        }

        val cancelButton = rootView.findViewById<MaterialButton>(R.id.cancel)
        cancelButton.setOnClickListener {
            mEditor.putBoolean("welcome", false)
            mEditor.apply()
            dismiss()
        }

        return rootView
    }
}