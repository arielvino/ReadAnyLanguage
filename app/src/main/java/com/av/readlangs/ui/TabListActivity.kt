package com.av.readlangs.ui

import android.content.Intent
import com.av.readlangs.R
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.core.view.marginTop
import com.av.readinlangs.App
import com.av.readlangs.ReadingTab
import java.io.File

class TabListActivity : ComponentActivity() {
    lateinit var tabsContainer: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_list_activity)

        tabsContainer = findViewById(R.id.lists_of_tabs)
        refreshList()
        val newTabButton = findViewById<Button>(R.id.new_tab_button)
        newTabButton.setOnClickListener {
            val intent = Intent(this, CreateTabActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    fun refreshList() {
        tabsContainer.removeAllViews()
        val tabs = ReadingTab.listTabs()
        for (tab in tabs) {
            val button = Button(this)
            button.text = tab
            button.setOnClickListener {
                val intent = Intent(this, ReadingActivity::class.java)
                intent.putExtra(ReadingActivity.TAB_NAME_KEY, tab)
                startActivity(intent)
            }
            button.setBackgroundColor(getColor(R.color.dark_container))
            button.setTextColor(getColor(R.color.dark_text))
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = 30
            button.layoutParams = layoutParams
            tabsContainer.addView(button)
        }
    }
}