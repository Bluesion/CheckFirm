package com.illusion.checkfirm.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.search.WebViewActivity

class SearchResultDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.dialog_search_result, container, false)

        val isOfficial = arguments!!.getBoolean("isOfficial")
        val model = arguments!!.getString("model")
        val csc = arguments!!.getString("csc")
        val latest = arguments!!.getString("latest")
        val previous = arguments!!.getString("previous")

        val latestTitle = rootView.findViewById<MaterialTextView>(R.id.latest_title)
        val latestText = rootView.findViewById<MaterialTextView>(R.id.latest_text)
        val changelog = rootView.findViewById<MaterialButton>(R.id.changelog)
        val copy = rootView.findViewById<MaterialButton>(R.id.copy)
        val share = rootView.findViewById<MaterialButton>(R.id.share)
        val previousTitle = rootView.findViewById<MaterialTextView>(R.id.previous_title)
        val list = rootView.findViewById<TextView>(R.id.list)

        if (isOfficial) {
            latestTitle.text = getString(R.string.latest_official)
            previousTitle.text = getString(R.string.previous_official)
            changelog.visibility = View.VISIBLE
            changelog.setOnClickListener {
                val link = "http://doc.samsungmobile.com/$model/$csc/doc.html"
                val intent = Intent(activity!!, WebViewActivity::class.java)
                intent.putExtra("url", link)
                intent.putExtra("number", 1)
                startActivity(intent)
            }
        } else {
            latestTitle.text = getString(R.string.latest_test)
            previousTitle.text = getString(R.string.previous_test)
            changelog.visibility = View.GONE
        }

        latestText.text = latest
        list.text = previous

        val clipboard = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        copy.setOnClickListener {
            val clip = ClipData.newPlainText("checkfirmLatest", latestText.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(activity!!, R.string.clipboard, Toast.LENGTH_SHORT).show()
        }

        share.setOnClickListener {
            val link = "https://checkfirm.com/$model/$csc"
            val clip = ClipData.newPlainText("checkfirmLink", link)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(activity!!, R.string.link_shared, Toast.LENGTH_SHORT).show()
        }

        val okButton = rootView.findViewById<MaterialButton>(R.id.ok)
        okButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    companion object {
        fun newInstance(isOfficial: Boolean, model: String, csc:String, latest: String, previous: String): SearchResultDialog {
            val f = SearchResultDialog()

            val args = Bundle()
            args.putBoolean("isOfficial", isOfficial)
            args.putString("model", model)
            args.putString("csc", csc)
            args.putString("latest", latest)
            args.putString("previous", previous)
            f.arguments = args

            return f
        }
    }
}