package com.illusion.checkfirm

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.illusion.checkfirm.dialogs.BookmarkDialog
import com.illusion.checkfirm.bookmark.Bookmark
import com.illusion.checkfirm.search.Search
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val catcher = sharedPrefs.getBoolean("catcher", false)
        if (catcher) {
            val model = sharedPrefs.getString("catcher_model", "CheckFirm") as String
            val csc = sharedPrefs.getString("catcher_csc", "Catcher") as String
            if (model.isNotBlank() && csc.isNotBlank()) {
                FirebaseMessaging.getInstance().subscribeToTopic(model+csc)
            }
        }

        initToolbar()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.hide()
        fab.setOnClickListener {
            val bottomSheetFragment = BookmarkDialog.newInstance(false, "", "", "", -1)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        tabLayout = findViewById(R.id.mTabLayout)
        tabLayout.addTab(tabLayout.newTab().setText(R.string.firmware))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.bookmark))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                val tabTextView = TextView(this)
                tab.customView = tabTextView
                tabTextView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                tabTextView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                tabTextView.text = tab.text
                if (i == 0) {
                    tabTextView.setTypeface(null, Typeface.BOLD)
                    tabTextView.setTextColor(Color.parseColor("#4297ff"))
                }
            }
        }
        val mViewPager = findViewById<ViewPager2>(R.id.mViewPager)
        mViewPager.offscreenPageLimit = 2
        mViewPager.adapter = MyAdapter(2)
        mViewPager.isUserInputEnabled = false
        mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        tabLayout.addOnTabSelectedListener(onTabSelectedListener(mViewPager))
    }

    private fun initToolbar() {
        val one = sharedPrefs.getBoolean("one", true)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        toolbar.overflowIcon = getDrawable(R.drawable.ic_more)
        setSupportActionBar(toolbar)
        val mAppBar = findViewById<AppBarLayout>(R.id.appbar)
        val height = (resources.displayMetrics.heightPixels * 0.3976)
        val lp = mAppBar.layoutParams
        lp.height = height.toInt()
        if (one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }
        val title = findViewById<TextView>(R.id.title)
        val expandedTitle = findViewById<TextView>(R.id.expanded_title)
        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })
    }

    private fun onTabSelectedListener(pager: ViewPager2): TabLayout.OnTabSelectedListener {
        return object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                val text = tab.customView as TextView
                text.setTypeface(null, Typeface.BOLD)
                text.setTextColor(Color.parseColor("#4297ff"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val text = tab.customView as TextView
                text.setTypeface(null, Typeface.NORMAL)
                text.setTextColor(Color.parseColor("#7A7A7A"))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }
        }
    }

    inner class MyAdapter internal constructor(private var numOfTabs: Int) : FragmentStateAdapter(supportFragmentManager, lifecycle) {
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return Search()
                1 -> return Bookmark()
            }
            return Search()
        }

        override fun getItemCount(): Int {
            return numOfTabs
        }
    }
}