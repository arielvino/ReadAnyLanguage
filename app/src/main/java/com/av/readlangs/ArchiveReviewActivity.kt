package com.av.readlangs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.av.readinlangs.App
import com.av.readlangs.learningArchive.WordItem
import com.av.readlangs.ui.theme.ReadLangsTheme

class ArchiveReviewActivity : ComponentActivity() {
    private lateinit var wordView: TextView
    private lateinit var revealButton: Button
    private lateinit var revealHolder: ViewGroup
    private lateinit var translationView: TextView
    private lateinit var explanationView: TextView
    private lateinit var nextWordButton: Button
    private lateinit var removeWordButton: Button
    private lateinit var editWordButton: Button
    private lateinit var editingPanel: ViewGroup
    private lateinit var translationInput: EditText
    private lateinit var explanationInput: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    private var words: List<WordItem>? = null
    private var wordIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.archive_review_activity)

        //word view:
        wordView = findViewById(R.id.word)

        //reveal button:
        revealButton = findViewById(R.id.revealTranslation_button)
        revealButton.setOnClickListener {
            if (words != null) {
                if (wordIndex < words!!.size) {
                    revealButton.visibility = View.GONE
                    revealHolder.visibility = View.VISIBLE
                }
            }
        }

        //reveal holder:
        revealHolder = findViewById(R.id.reveal_holder)

        //translation view:
        translationView = findViewById(R.id.translation)

        //explanation view:
        explanationView = findViewById(R.id.explanation)

        //next button:
        nextWordButton = findViewById(R.id.nextWord_button)
        nextWordButton.setOnClickListener {
            loadNextWord()
        }

        //remove button:
        removeWordButton = findViewById(R.id.removeWord_button)
        removeWordButton.setOnClickListener {
            App.archive.deleteWord(words!![wordIndex].word)
            refreshWordsList()
            loadWord()
        }

        //edit button:
        editWordButton = findViewById(R.id.editWord_button)
        editWordButton.setOnClickListener {
            editingPanel.visibility = View.VISIBLE
            revealHolder.visibility = View.GONE
            revealButton.visibility = View.GONE
            translationInput.setText(words!![wordIndex].translation)
            explanationInput.setText(words!![wordIndex].explanation)
        }

        //editing panel:
        editingPanel = findViewById(R.id.edit_panel)

        //translation input:
        translationInput = findViewById(R.id.translation_input)

        //explanation input:
        explanationInput = findViewById(R.id.explanation_input)

        //save button:
        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener {
                val translation = translationInput.text
                //do nothing if the translation is empty:
                if (translation.isBlank()) {
                    return@setOnClickListener
                }

                var explanation: String? = explanationInput.text.toString()
                //explanation might be null:
                if (explanation!!.isBlank()) {
                    explanation = null
                }

                //save, refresh and move on:
                App.archive.editWord(words!![wordIndex].word, translation.toString(), explanation)
                refreshWordsList()
                loadNextWord()
        }

        //cancel button:
        cancelButton = findViewById(R.id.cancel_button)
        cancelButton.setOnClickListener {
            editingPanel.visibility = View.GONE
            revealHolder.visibility = View.VISIBLE
            translationInput.setText("")
            explanationInput.setText("")
        }

        //start session:
        refreshWordsList()
        loadWord()
    }

    private fun loadNextWord() {
        wordIndex++
        loadWord()
    }

    private fun loadWord() {
        if (words != null) {
            //if exceed the number of words - go back to the start:
            if (words!!.lastIndex < wordIndex) {
                wordIndex = 0
            }

            //if there are any words:
            if (words!!.lastIndex >= wordIndex) {
                revealButton.visibility = View.VISIBLE
                revealHolder.visibility = View.GONE
                editingPanel.visibility = View.GONE
                wordView.text = words!![wordIndex].word
                translationView.text = words!![wordIndex].translation
                if (words!![wordIndex].explanation != null) {
                    explanationView.text = words!![wordIndex].explanation
                }
                translationInput.setText("")
                explanationInput.setText("")
            }

            //if there are no words:
            else{
                finish()
            }
        }
        //there is a problem with loading the archive:
        else{
            Toast.makeText(this, "Error loading words.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun refreshWordsList() {
        words = App.archive.getWords()
    }
}


@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    ReadLangsTheme {
        Greeting2("Android")
    }
}