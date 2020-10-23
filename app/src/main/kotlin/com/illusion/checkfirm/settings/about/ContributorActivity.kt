package com.illusion.checkfirm.settings.about

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivityContributorBinding
import com.illusion.checkfirm.etc.WebViewActivity

class ContributorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityContributorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.title = getString(R.string.contributor)
        setSupportActionBar(toolbar)

        val designer = binding.designer
        designer.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("work@newfitlab.com"))
            startActivity(intent)
        }

        val developer = binding.developer
        developer.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("dnjscjf098@gmail.com"))
            startActivity(intent)
        }

        val github = binding.github
        github.setOnClickListener {
            val link = "https://github.com/gpillusion/CheckFirm"
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("url", link)
            intent.putExtra("number", 1)
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
