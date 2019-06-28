package com.illusion.checkfirm.help

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.card.MaterialCardView
import com.illusion.checkfirm.R

class HelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val search = findViewById<MaterialCardView>(R.id.search)
        search.setOnClickListener {
            val intent = Intent(this, HelpSearch::class.java)
            startActivity(intent)
        }

        val firmware = findViewById<MaterialCardView>(R.id.firmware)
        firmware.setOnClickListener {
            val intent = Intent(this, HelpFirmware::class.java)
            startActivity(intent)
        }

        val functions = findViewById<MaterialCardView>(R.id.functions)
        functions.setOnClickListener {
            val intent = Intent(this, HelpFunctions::class.java)
            startActivity(intent)
        }

        val questions = findViewById<MaterialCardView>(R.id.questions)
        questions.setOnClickListener {
            val intent = Intent(this, HelpQuestions::class.java)
            startActivity(intent)
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