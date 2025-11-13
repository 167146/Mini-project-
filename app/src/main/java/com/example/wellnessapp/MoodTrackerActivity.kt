package com.example.wellnessapp

import android.content.Intent
import android.os.Bundle

class MoodTrackerActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_tracker)


        val moodClickListener = { mood: String ->
            moodViewModel.addMood(mood) {
                runOnUiThread {
                    val intent = Intent(this, JournalActivity::class.java)
                    intent.putExtra("mood", mood)
                    startActivity(intent)
                }
            }
        }

        happyButton.setOnClickListener { moodClickListener("Happy") }
        sadButton.setOnClickListener { moodClickListener("Sad") }
        anxiousButton.setOnClickListener { moodClickListener("Anxious") }
        gratefulButton.setOnClickListener { moodClickListener("Grateful") }
    }
}