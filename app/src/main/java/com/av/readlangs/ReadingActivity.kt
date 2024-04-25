package com.av.readlangs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.av.readinlangs.App
import com.av.readinlangs.FileUtils
import com.av.readinlangs.ITranslationProvider
import com.av.readlangs.MyEditText.OnSelectionChangedListener
import com.av.readlangs.learningArchive.WordItem

class ReadingActivity : ComponentActivity() {
    val forbiddenCharacters = "., \n\r\t=+\"\'«»(){}[]?!"
    val contentFilePath = App.appContext.cacheDir.absolutePath + "/" + "content_save"
    val scrollFilePath = App.appContext.cacheDir.absolutePath + "/" + "scroll_save"

    lateinit var result: TextView
    lateinit var textBox: MyEditText

    var word: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reading_activity)
        App.translator.registerForTranslationReceiving(object :
            ITranslationProvider.OnTranslationReceived {
            override fun onTranslationReceived(origin: String, translation: String?) {
                //show the result to the user:
                runOnUiThread {
                    if (translation == null) {
                        result.text = ""
                    } else {
                        result.text = translation
                    }
                }
            }
        })

        //result panel:
        result = findViewById(R.id.result)

        //textBox:
        textBox = findViewById(R.id.textbox)
        textBox.showSoftInputOnFocus = false

        //on selection changed:
        textBox.addOnSelectionChangedListener(object : OnSelectionChangedListener {
            override fun onSelectionChanged(selStart: Int, selEnd: Int) {

                var start = textBox.selectionStart
                var end = textBox.selectionEnd

                if (start > end) {
                    val temp = start
                    start = end
                    end = temp
                }

                //get the word you need to translate:
                word = detectWord(
                    textBox.text.toString(),
                    start,
                    end
                )

                //translate:
                if (word.length in 1..99) {
                    App.translator.requestTranslation(word)
                }
            }
        })

        //on text changed:
        textBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                FileUtils.writeFile(contentFilePath, s.toString())
            }
        })

        //on scroll changed:
        textBox.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            FileUtils.writeFile(
                scrollFilePath,
                scrollY.toString()
            )
        }

        //insertWord button:
        val insertWordButton: Button = findViewById(R.id.insertWord_button)
        insertWordButton.setOnClickListener {
            if (word.isNotBlank() && result.text.toString().isNotBlank()) {

                //save the result to archive:
                App.archive.saveWord(WordItem(word, result.text.toString()))

                //insert translation into the text:
                //todo: warning: is the direction of the '(' and ')' depending on the rtl direction of the source language?
                val (_, end) = detectWordBorders(
                    textBox.text.toString(),
                    textBox.selectionStart,
                    textBox.selectionEnd
                )
                val newText = textBox.text.insert(end, "(" + result.text.toString() + ")")
            }
        }

        //load text and scroll value from cash after the activity is fully initialized:
        result.text = ""

        //load text:
        val content = FileUtils.readFile(contentFilePath)
        if (content != null) {
            textBox.setText(content)
        }

        //load scroll:
        textBox.post {
            val scroll = FileUtils.readFile(scrollFilePath)
            if (scroll != null) {
                if (scroll.toIntOrNull() != null) {
                    textBox.scrollY = scroll.toInt()
                }
            }
        }
    }

    fun detectWord(text: String, selStart: Int, selEnd: Int): String {
        val (start, end) = detectWordBorders(text, selStart, selEnd)

        //the reason for this conditioning is that if the end-selector is at the end of the text - running substring(start, end) throw IndexOutOfBorderException:
        var word = text.substring(start)
        if (end < text.length) {
            word = text.substring(start, end)
        }

        return word
    }

    private fun detectWordBorders(text: String, selStart: Int, selEnd: Int): Pair<Int, Int> {
        var start = selStart
        var end = selEnd

        //track the start of the word:
        while (start > 0) {
            if (!forbiddenCharacters.contains(text[start - 1])) {
                start--
            } else {
                break
            }
        }

        //track the end of the word:
        while (end < text.length) {
            if (!forbiddenCharacters.contains(text[end])) {
                end++
            } else {
                break
            }
        }

        return Pair(start, end)
    }
}