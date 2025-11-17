package com.example.wellnessapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wellnessapp.data.database.AppDatabase
import com.example.wellnessapp.data.entities.JournalEntry
import com.example.wellnessapp.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JournalActivity : AppCompatActivity() {

    private lateinit var repository: AppRepository
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        userId = intent.getIntExtra("USER_ID", -1)

        val journalInput = findViewById<EditText>(R.id.journalEntry)
        val saveButton = findViewById<Button>(R.id.saveJournalButton)

        saveButton.setOnClickListener {
            val text = journalInput.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, "Please write something", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val entry = JournalEntry(userId = userId, title = "My Journal", content = text, date = System.currentTimeMillis())

            lifecycleScope.launch(Dispatchers.IO) {
                repository.insertJournal(entry)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@JournalActivity, "Journal saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
