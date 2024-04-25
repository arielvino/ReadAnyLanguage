package com.av.readlangs

import android.R.string
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.av.readinlangs.App
import com.av.readlangs.supportedLanguages.LanguageItem

class SettingsActivity : ComponentActivity() {

    private val languages = App.supportedLanguagesProvider.getSupportedLanguages()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)



        val adapter = LanguageAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val sourceSpinner = findViewById<Spinner>(R.id.sl_spinner)
        sourceSpinner.adapter = adapter
        sourceSpinner.setSelection(getLanguageId(App.sourceLanguage))

        val targetSpinner = findViewById<Spinner>(R.id.tl_spinner)
        targetSpinner.adapter = adapter
        targetSpinner.setSelection(getLanguageId(App.targetLanguage))

        // Set any listeners if needed for sourceSpinner
        sourceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguage = parent.getItemAtPosition(position) as LanguageItem

                App.sourceLanguage = selectedLanguage.code
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected for sourceSpinner
            }
        }

        // Set any listeners if needed for targetSpinner
        targetSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguage = parent.getItemAtPosition(position) as LanguageItem
                App.targetLanguage = selectedLanguage.code
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected for targetSpinner
            }
        }
    }

    fun getLanguageId(languageCode:String):Int{
        for ((index, value) in languages.withIndex()){
            if(value.code.contentEquals(languageCode)){
                return index
            }
        }

        //return zero as default:
        return 0
    }
}

class LanguageAdapter(context: Context, resource: Int, objects: List<LanguageItem>) :
    ArrayAdapter<LanguageItem>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val language = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = language?.name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val language = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = language?.name
        return view
    }
}