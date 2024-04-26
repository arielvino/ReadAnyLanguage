package com.av.readlangs

import android.R.string
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.av.readinlangs.App
import com.av.readlangs.supportedLanguages.LanguageItem

class SettingsActivity : ComponentActivity() {

    private val languages = App.supportedLanguagesProvider.getSupportedLanguages()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        //Google api key change:
        val apiKeyInput = findViewById<EditText>(R.id.api_key_input)
        val saveKeyButton = findViewById<Button>(R.id.save_key_button)
        saveKeyButton.setOnClickListener {
            GoogleApiKey.key = apiKeyInput.text.toString()
            apiKeyInput.setText("")
            Toast.makeText(this, "Key saved.", Toast.LENGTH_SHORT).show()
        }

        //Spinner for choosing source and target languages:
        val adapter = LanguageAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val sourceSpinner = findViewById<Spinner>(R.id.sl_spinner)
        sourceSpinner.adapter = adapter
        sourceSpinner.setSelection(getLanguageId(App.sourceLanguage))

        val targetSpinner = findViewById<Spinner>(R.id.tl_spinner)
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

                App.sourceLanguage = selectedLanguage.code
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