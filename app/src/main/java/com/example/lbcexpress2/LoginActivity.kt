package com.example.lbcexpress2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginscreen)

        // 1. Initialize Views
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val btnGoogle = findViewById<LinearLayout>(R.id.btnGoogle)
        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)

        // 2. Google Sign In Mock
        btnGoogle.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_EMAIL", "google_user@gmail.com")
                putExtra("EXTRA_PASSWORD", "Logged in via Google")
            }
            startActivity(intent)
        }

        // 3. Email/Password Sign In Logic
        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validate(email, password, etEmail, etPassword)) {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("EXTRA_EMAIL", email)
                    putExtra("EXTRA_PASSWORD", password)
                }
                startActivity(intent)
            }
        }

        // 4. Navigation to Sign Up Activity
        // Note: Make sure SignUpActivity is registered in AndroidManifest.xml!
        tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validate(email: String, pass: String, eEmail: EditText, ePass: EditText): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            eEmail.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            eEmail.error = "Invalid email format"
            isValid = false
        }

        if (pass.isEmpty()) {
            ePass.error = "Password is required"
            isValid = false
        } else if (pass.length < 6) {
            ePass.error = "Min 6 characters"
            isValid = false
        }

        return isValid
    }
}