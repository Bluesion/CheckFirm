package com.illusion.checkfirm.dialogs

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.illusion.checkfirm.R

class SearchDialog: BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.dialog_search, container, false)

        val title = rootView.findViewById<TextView>(R.id.title)
        val info = rootView.findViewById<ImageView>(R.id.info)
        val model = arguments!!.getString("model")
        val csc = arguments!!.getString("csc")
        info.setOnClickListener {
            val link = "http://doc.samsungmobile.com/$model/$csc/doc.html"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
        val list = rootView.findViewById<TextView>(R.id.list)
        val firmware = arguments!!.getString("firmware")
        val isOfficial = arguments!!.getBoolean("isOfficial")
        if (isOfficial) {
            title.text = getString(R.string.previous_official)
            info.visibility = View.VISIBLE
            list.text = firmware
        } else {
            title.text = getString(R.string.previous_test)
            info.visibility = View.GONE
            list.text = firmware
        }

        val okButton = rootView.findViewById<MaterialButton>(R.id.ok)
        okButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    companion object {
        fun newInstance(isOfficial: Boolean, firmware: String, model: String, csc: String): SearchDialog {
            val f = SearchDialog()

            val args = Bundle()
            args.putBoolean("isOfficial", isOfficial)
            args.putString("firmware", firmware)
            args.putString("model", model)
            args.putString("csc", csc)
            f.arguments = args

            return f
        }
    }
}