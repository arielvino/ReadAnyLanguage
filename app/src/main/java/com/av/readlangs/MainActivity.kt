package com.av.readlangs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        //read button:
        val readButton: Button = findViewById(R.id.readButton)
        readButton.setOnClickListener {
            val intent = Intent(this, ReadingActivity::class.java)
            startActivity(intent)
        }

        //review button:
        val reviewButton:Button = findViewById(R.id.reviewButton)
        reviewButton.setOnClickListener{
            val intent = Intent(this, ArchiveReviewActivity::class.java)
            startActivity(intent)
        }

        //settings button:
        val settingsButton:Button = findViewById(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}