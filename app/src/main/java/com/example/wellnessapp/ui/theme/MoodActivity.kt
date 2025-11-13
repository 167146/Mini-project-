package com.example.wellnessapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MoodActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Call your Compose screen here
            MoodScreen()
        }
    }
}
@Composable
fun MoodScreen(viewModel: MoodViewModel = viewModel(factory = MoodViewModelFactory(AppRepository(AppDatabase.getDatabase(LocalContext.current).moodDao())))) {

    val moods by viewModel.allMoods.observeAsState(emptyList())

    // Use `moods` to show your UI
}

