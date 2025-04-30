package vcmsa.projects.smartspend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider


class LoginActivity : AppCompatActivity() {
        private lateinit var usernameEditText: EditText
        private lateinit var passwordEditText: EditText
        private lateinit var loginButton: Button
        private lateinit var viewModel: HomeViewModel

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)

            usernameEditText = findViewById(R.id.usernameEditText)
            passwordEditText = findViewById(R.id.passwordEditText)
            loginButton = findViewById(R.id.loginButton)
            viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

            loginButton.setOnClickListener {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()

                if (username.isNotBlank() && password.isNotBlank()) {
                    // Perform login (validate against database)
                    viewModel.getUser(username).observe(this){ user ->
                        if (user != null && password == user.passwordHash) { //Simplified, use hashing
                            // Login successful
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            // Start the main activity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Close the login activity
                        } else {
                            // Login failed
                            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
