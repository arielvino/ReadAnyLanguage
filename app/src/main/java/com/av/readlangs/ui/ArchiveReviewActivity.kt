package com.av.readlangs.ui

import com.av.readlangs.R
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.av.readinlangs.App
import com.av.readlangs.learningArchive.WordItem
import java.security.SecureRandom
import kotlin.math.abs

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
    private var word: WordItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.archive_review_activity)

        //word view:
        wordView = findViewById(R.id.word)

        //reveal button:
        revealButton = findViewById(R.id.revealTranslation_button)
        revealButton.setOnClickListener {
            if (word != null) {
                revealButton.visibility = View.GONE
                revealHolder.visibility = View.VISIBLE
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
            loadWord()
        }

        //remove button:
        removeWordButton = findViewById(R.id.removeWord_button)
        removeWordButton.setOnClickListener {
            App.archive.deleteWord(word!!.word)
            refreshWordsList()
            loadWord()
        }

        //edit button:
        editWordButton = findViewById(R.id.editWord_button)
        editWordButton.setOnClickListener {
            editingPanel.visibility = View.VISIBLE
            revealHolder.visibility = View.GONE
            revealButton.visibility = View.GONE
            translationInput.setText(word!!.translation)
            explanationInput.setText(word!!.explanation)
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
            App.archive.editWord(word!!.word, translation.toString(), explanation)
            refreshWordsList()
            loadWord()
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

    private fun loadWord() {
        if (words != null) {
            if (words!!.isNotEmpty()) {
                word = words!![abs(SecureRandom().nextInt()) % words!!.size]

                revealButton.visibility = View.VISIBLE
                revealHolder.visibility = View.GONE
                editingPanel.visibility = View.GONE
                wordView.text = word!!.word
                translationView.text = word!!.translation
                if (word!!.explanation != null) {
                    explanationView.text = word!!.explanation
                } else {
                    explanationView.text = ""
                }
                translationInput.setText("")
                explanationInput.setText("")
            }

            //if there are no words:
            else {
                finish()
            }
        }
        //there is a problem with loading the archive:
        else {
            Toast.makeText(this, "No words found for these languages.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun refreshWordsList() {
        words = App.archive.getWords()
    }
}