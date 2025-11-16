package com.example.wellnessapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import com.example.wellnessapp.data.database.AppDatabase
import com.example.wellnessapp.data.entities.MoodEntry
import com.example.wellnessapp.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoodTrackerActivity : AppCompatActivity() {

    private lateinit var repository: AppRepository
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_tracker)

        // Retrieve user ID from login
        userId = intent.getIntExtra("USER_ID", -1)

        val db = AppDatabase.getDatabase(this)
        repository = AppRepository(db.userDao(), db.moodDao(), db.journalDao())

        val happyBtn = findViewById<Button>(R.id.happyButton)
        val sadBtn = findViewById<Button>(R.id.sadButton)
        val anxiousBtn = findViewById<Button>(R.id.anxiousButton)
        val gratefulBtn = findViewById<Button>(R.id.gratefulButton)

        happyBtn.setOnClickListener { saveMood("Happy") }
        sadBtn.setOnClickListener { saveMood("Sad") }
        anxiousBtn.setOnClickListener { saveMood("Anxious") }
        gratefulBtn.setOnClickListener { saveMood("Grateful") }
    }

    private fun saveMood(mood: String) {
        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            return
        }

        val moodEntry = MoodEntry(
            userId = userId,
            mood = mood,
            timestamp = System.currentTimeMillis()
        )

        lifecycleScope.launch(Dispatchers.IO) {
            repository.saveMood(moodEntry)

            withContext(Dispatchers.Main) {
                Toast.makeText(this@MoodTrackerActivity, "Mood saved!", Toast.LENGTH_SHORT).show()

                // Redirect to journal page
                val intent = Intent(this@MoodTrackerActivity, JournalActivity::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
            }
        }
    }
}
