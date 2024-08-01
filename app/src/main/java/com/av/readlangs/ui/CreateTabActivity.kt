package com.av.readlangs.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.av.readinlangs.App
import com.av.readlangs.R
import com.av.readlangs.ReadingTab
import com.av.readlangs.supportedLanguages.LanguageItem
import java.io.File

class CreateTabActivity : ComponentActivity() {
    private val languages = App.supportedLanguagesProvider.getSupportedLanguages()

    lateinit var tabNameInput: EditText
    lateinit var createButton: Button
    lateinit var sourceSpinner:Spinner
    lateinit var targetSpinner:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_tab_activity)
        tabNameInput = findViewById(R.id.tab_name_input)
        createButton = findViewById(R.id.create_tab_button)
        sourceSpinner = findViewById(R.id.sl_spinner)
        targetSpinner = findViewById(R.id.tl_spinner)

        //Spinner for choosing source and target languages:
        val adapter = LanguageAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sourceSpinner.adapter = adapter
        sourceSpinner.setSelection(getLanguageId(App.sourceLanguage))

        targetSpinner.adapter = adapter
        targetSpinner.setSelection(getLanguageId(App.targetLanguage))

        sourceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguage = parent.getItemAtPosition(position) as LanguageItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected for sourceSpinner
            }
        }

        targetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected for targetSpinner
            }
        }

        createButton.setOnClickListener {
            val name = tabNameInput.text.toString()
            if (name.isNullOrBlank()) {
                Toast.makeText(this, "You must specify a name.", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }

            //todo: check if the name is a valid directory name

            val existingTabs = ReadingTab.listTabs()
            for (tab in existingTabs) {
                if (tab.contentEquals(name)) {
                    Toast.makeText(
                        this,
                        "Tab with the same name already exists.",
                        Toast.LENGTH_SHORT
                    )
                    return@setOnClickListener
                }
            }

            val sl = (sourceSpinner.selectedItem as LanguageItem).code
            val tl = (targetSpinner.selectedItem as LanguageItem).code
            if (sl.contentEquals(tl)) {
                Toast.makeText(this, "You better select different languages...", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }

            val directory =File(ReadingTab.tabDirectoryPath + "/" + name)
                try {
                    directory.mkdir()
                }
                catch (e:Exception){
                    Toast.makeText(this, "Error creating tab. Consider trying another name.", Toast.LENGTH_SHORT)
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT)
                    return@setOnClickListener
                }
            val tab = ReadingTab(tabNameInput.text.toString())
            tab.sourceLanguage = sl
            tab.targetLanguage = tl
            finish()
        }
    }

    fun getLanguageId(languageCode: String): Int {
        for ((index, value) in languages.withIndex()) {
            if (value.code.contentEquals(languageCode)) {
                return index
            }
        }

        //return zero as default:
        return 0
    }
}