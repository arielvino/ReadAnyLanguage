package com.av.readlangs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.av.readinlangs.App
import com.av.readinlangs.FileUtils
import com.av.readinlangs.ITranslationProvider
import com.av.readlangs.MyEditText.OnSelectionChangedListener
import com.av.readlangs.learningArchive.WordItem
import com.av.readlangs.ui.theme.ReadLangsTheme

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

                //get the word you need to translate:
                word = detectWord(
                    textBox.text.toString(),
                    textBox.selectionStart,
                    textBox.selectionEnd
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
        insertWordButton.setOnClickListener(View.OnClickListener
        {
            if (word.isNotBlank() && result.text.toString().isNotBlank()) {

                //save the result to archive:
                App.archive.saveWord(WordItem(word, result.text.toString()))
            }
        })

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
        var start: Int = selStart
        var end: Int = selEnd

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

        //the reason for this conditioning is that if the end-selector is at the end of the text - running substring(start, end) throw IndexOutOfBorderException:
        var word = text.substring(start)
        if (end < text.length) {
            word = text.substring(start, end)
        }

        return word
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ReadLangsTheme {
        Column {
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { /*TODO*/ }) {
                Text(text = "Insert Word")
            }
            TextField(modifier = Modifier.fillMaxSize(), value = "lll", onValueChange = {})
            Text(text = "hhhh")
        }
    }
}