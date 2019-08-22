package com.illusion.checkfirm.help

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.card.MaterialCardView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.dialogs.HelpDialog

class HelpSearch : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_search)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            finish()
        }

        val myDevice = findViewById<TextView>(R.id.my_device)
        myDevice.text = String.format(getString(R.string.search_1), Build.MODEL)

        val search = findViewById<ImageView>(R.id.search)
        val card0 = findViewById<MaterialCardView>(R.id.helpCard_0)
        val device = findViewById<TextView>(R.id.help_device)
        val card1 = findViewById<MaterialCardView>(R.id.helpCard_1)

        val model = findViewById<EditText>(R.id.model)
        val csc = findViewById<EditText>(R.id.csc)
        model.setSelection(model.text.length)

        search.setOnClickListener {
            device.text = String.format(getString(R.string.device_format),
                    model.text!!.toString().trim().toUpperCase(), csc.text!!.toString().trim().toUpperCase())
            card0.visibility = View.VISIBLE
        }

        card0.setOnClickListener {
            val bottomSheetFragment = HelpDialog()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            card1.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}