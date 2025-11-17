package com.example.wellnessapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wellnessapp.data.database.AppDatabase
import com.example.wellnessapp.data.entities.JournalEntry
import com.example.wellnessapp.data.entities.MoodEntry
import com.example.wellnessapp.data.entities.User
import com.example.wellnessapp.data.repository.AppRepository
import com.example.wellnessapp.ui.theme.WellnessAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var repository: AppRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database & repository
        val db = AppDatabase.getDatabase(this)
        repository = AppRepository(db.userDao(), db.moodDao(), db.journalDao())

        setContent {
            WellnessAppTheme {
                val navController = rememberNavController()
                val currentUserId = remember { mutableStateOf(-1) }
                val currentUsername = remember { mutableStateOf("") }

                NavHost(navController = navController, startDestination = "signup") {

                    composable("signup") {
                        SignupScreen(navController) { user ->
                            currentUserId.value = user.id
                            currentUsername.value = user.username
                        }
                    }

                    composable("login") {
                        LoginScreen(navController) { user ->
                            currentUserId.value = user.id
                            currentUsername.value = user.username
                        }
                    }

                    composable("welcome") {
                        WelcomeScreen(navController, currentUsername.value)
                    }

                    composable("dashboard") {
                        DashboardScreen(navController, currentUsername.value)
                    }

                    composable("mood") {
                        MoodTrackerScreen(repository, currentUserId.value)
                    }

                    composable("journal") {
                        JournalScreen(repository, currentUserId.value) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }

    // ------------------- Signup Screen -------------------
    @Composable
    fun SignupScreen(
        navController: androidx.navigation.NavHostController,
        onSignupSuccess: (User) -> Unit
    ) {
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (name.isBlank() || email.isBlank() || password.isBlank()) return@Button

                val newUser = User(username = name, email = email, password = password)
                lifecycleScope.launch {
                    val existing = withContext(Dispatchers.IO) { repository.getUserByEmail(email) }
                    if (existing != null) {
                        Toast.makeText(context, "Email already registered", Toast.LENGTH_SHORT).show()
                    } else {
                        withContext(Dispatchers.IO) { repository.registerUser(newUser) }
                        val createdUser = withContext(Dispatchers.IO) { repository.getUserByEmail(email) }
                        createdUser?.let { onSignupSuccess(it) }
                        Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
                        navController.navigate("welcome") {
                            popUpTo("signup") { inclusive = true }
                        }
                    }
                }
            }) {
                Text("Sign Up")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Already have an account? Login")
            }
        }
    }

    // ------------------- Login Screen -------------------
    @Composable
    fun LoginScreen(
        navController: androidx.navigation.NavHostController,
        onLoginSuccess: (User) -> Unit
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (email.isBlank() || password.isBlank()) return@Button

                lifecycleScope.launch {
                    val user = withContext(Dispatchers.IO) { repository.login(email, password) }
                    if (user != null) {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                        onLoginSuccess(user)
                        navController.navigate("welcome") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("signup") }) {
                Text("Don't have an account? Sign Up")
            }
        }
    }

    // ------------------- Welcome Screen -------------------
    @Composable
    fun WelcomeScreen(navController: androidx.navigation.NavHostController, username: String) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome, $username!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("dashboard") }) {
                Text("Go to Dashboard")
            }
        }
    }

    // ------------------- Dashboard -------------------
    @Composable
    fun DashboardScreen(navController: androidx.navigation.NavHostController, username: String) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Dashboard - $username", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("mood") }) { Text("Mood Tracker") }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.navigate("journal") }) { Text("Journal") }
        }
    }

    // ------------------- Mood Tracker Screen -------------------
    @Composable
    fun MoodTrackerScreen(repository: AppRepository, userId: Int) {
        var toastMessage by remember { mutableStateOf("") }
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(toastMessage) {
            if (toastMessage.isNotEmpty()) {
                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                toastMessage = ""
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Mood Tracker", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    coroutineScope.launch {
                        if (userId == -1) {
                            toastMessage = "User not found"
                        } else {
                            repository.insertMood(
                                MoodEntry(userId = userId, mood = "Happy", timestamp = System.currentTimeMillis())
                            )
                            toastMessage = "Mood saved!"
                        }
                    }
                }) { Text("Happy") }

                Button(onClick = {
                    coroutineScope.launch {
                        if (userId == -1) {
                            toastMessage = "User not found"
                        } else {
                            repository.insertMood(
                                MoodEntry(userId = userId, mood = "Sad", timestamp = System.currentTimeMillis())
                            )
                            toastMessage = "Mood saved!"
                        }
                    }
                }) { Text("Sad") }
            }
        }
    }

    // ------------------- Journal Screen -------------------
    @Composable
    fun JournalScreen(
        repository: AppRepository,
        userId: Int,
        onFinish: () -> Unit
    ) {
        var journalText by remember { mutableStateOf("") }
        var toastMessage by remember { mutableStateOf("") }
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(toastMessage) {
            if (toastMessage.isNotEmpty()) {
                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                toastMessage = ""
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Journal", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = journalText,
                onValueChange = { journalText = it },
                label = { Text("Write your thoughts...") },
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (journalText.isBlank()) {
                    toastMessage = "Please write something"
                    return@Button
                }

                coroutineScope.launch {
                    repository.insertJournal(
                        JournalEntry(
                            userId = userId,
                            title = "My Journal",
                            content = journalText,
                            date = System.currentTimeMillis()
                        )
                    )
                    toastMessage = "Journal saved!"
                    onFinish()
                }
            }) {
                Text("Save Journal")
            }
        }
    }
}
