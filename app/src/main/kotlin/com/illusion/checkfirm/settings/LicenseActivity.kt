package com.illusion.checkfirm.settings

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.illusion.checkfirm.R
import com.illusion.checkfirm.utils.ThemeChanger
import java.io.IOException
import java.io.InputStream

class LicenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeChanger.setAppTheme(this)
        setContentView(R.layout.activity_license)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val txtContent = findViewById<TextView>(R.id.txtContent)

        val assetManager = assets

        val input: InputStream
        try {
            input = assetManager.open("License.txt")

            val size = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            input.close()

            val text = String(buffer)

            txtContent.text = text
        } catch (ignored: IOException) {}
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