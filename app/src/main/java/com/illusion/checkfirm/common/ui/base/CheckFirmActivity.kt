package com.illusion.checkfirm.common.ui.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.features.settings.viewmodel.SettingsViewModel
import kotlin.jvm.java

abstract class CheckFirmActivity<VB: ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB
    protected lateinit var settingsViewModel: SettingsViewModel

    protected abstract fun createBinding(): VB

    /**
     * Set the insets for view. You should not set the insets for the toolbar.
     * Toolbar insets are handled by the [initToolbar] method.
     * You can use [setInset], [setTopInset], [setBottomInset], [setVerticalInset], [setHorizontalInset] to set the insets for the view.
     */
    protected abstract fun setContentInset()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        binding = createBinding()
        setContentView(binding.root)

        setContentInset()
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

    fun initToolbar(toolbar: ViewGroup, title: String, hasSubTitle: Boolean = false) {
        if (toolbar is MaterialToolbar) {
            toolbar.title = title
            setSupportActionBar(toolbar)
            setToolbarInset(toolbar, false)
        } else {
            val appBar = toolbar as AppBarLayout
            val collapsingToolbar = appBar.children.find { it.id == R.id.collapsing_toolbar } as CollapsingToolbarLayout
            val expandedLayout = collapsingToolbar.children.find { it.id == R.id.expanded_layout } as LinearLayout
            val expandedTitle = expandedLayout.children.find { it.id == R.id.expanded_title } as MaterialTextView
            val expandedSubTitle = expandedLayout.children.find { it.id == R.id.expanded_sub_title } as MaterialTextView
            val materialToolbar = collapsingToolbar.children.find { it.id == R.id.toolbar } as MaterialToolbar
            val toolbarTitle = materialToolbar.children.find { it.id == R.id.title } as MaterialTextView

            setSupportActionBar(materialToolbar)
            toolbarTitle.text = title
            expandedTitle.text = title
            appBar.apply {
                layoutParams.height = (resources.displayMetrics.heightPixels * 0.3976).toInt()
                addOnOffsetChangedListener { appBarLayout, _ ->
                    val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
                    expandedTitle.alpha = 1 - (percentage * 2 * -1)
                    expandedSubTitle.alpha = 1 - (percentage * 2 * -1)
                    toolbarTitle.alpha = percentage * -1
                }
            }

            if (hasSubTitle) {
                expandedSubTitle.visibility = View.VISIBLE
            }
            setToolbarInset(toolbar, true)
        }
    }

    fun setInset(view: View, dp: Int) {
        val contentPadding = Tools.dpToPx(this, dp.toFloat())
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.updatePadding(contentPadding)
            insets
        }
    }

    fun setToolbarInset(view: View, isExpandable: Boolean) {
        val contentPadding = Tools.dpToPx(this, 20F)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    or WindowInsetsCompat.Type.displayCutout())
            if (isExpandable) {
                v.setPadding(0, systemBars.top, 0, 0)
            } else {
                v.setPadding(0, systemBars.top + contentPadding, 0, 0)
            }
            insets
        }
    }

    fun setTopInset(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    or WindowInsetsCompat.Type.displayCutout())
            if (v.marginTop == systemBars.top) {
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = systemBars.top
                }
            } else {
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    topMargin = systemBars.top + view.marginTop
                }
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    fun setTopInset(view: View, dp: Int) {
        val contentPadding = Tools.dpToPx(this, dp.toFloat())
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.updatePadding(top = contentPadding + view.paddingTop)
            insets
        }
    }

    fun setBottomInset(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    or WindowInsetsCompat.Type.displayCutout())
            if (v.marginBottom == systemBars.bottom) {
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = systemBars.bottom
                }
            } else {
                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = systemBars.bottom + view.marginBottom
                }
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    fun setBottomInset(view: View, dp: Int) {
        val contentPadding = Tools.dpToPx(this, dp.toFloat())
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    or WindowInsetsCompat.Type.displayCutout())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = systemBars.bottom + contentPadding
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    fun setVerticalInset(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    or WindowInsetsCompat.Type.displayCutout())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = systemBars.top
                bottomMargin = systemBars.bottom
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    fun setVerticalInset(view: View, dp: Int) {
        setTopInset(view, dp)
        setBottomInset(view, dp)
    }

    fun setHorizontalInset(view: View, dp: Int) {
        val contentPadding = Tools.dpToPx(this, dp.toFloat())
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = contentPadding
                rightMargin = contentPadding
            }
            WindowInsetsCompat.CONSUMED
        }
    }
}