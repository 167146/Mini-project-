package com.example.wellnessapp

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity

class SignupActivity :  AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Navigate to LoginActivity
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }
}