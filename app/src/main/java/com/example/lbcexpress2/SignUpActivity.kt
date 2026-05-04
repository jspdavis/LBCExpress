package com.example.lbcexpress2

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signupscreen)

        val etFirst = findViewById<EditText>(R.id.etFirstName)
        val etEmail = findViewById<EditText>(R.id.etEmailSignUp)
        val etPassword = findViewById<EditText>(R.id.etPasswordSignUp)
        val btnSignUp = findViewById<Button>(R.id.btnSignUpSubmit)
        val tvSignInLink = findViewById<TextView>(R.id.tvSignInLink)

        btnSignUp.setOnClickListener {
            val fullName = etFirst.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please complete all required fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Invalid email format"
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val savedEmail = sharedPref.getString("KEY_EMAIL", "")
            if (!savedEmail.isNullOrBlank() && savedEmail.equals(email, ignoreCase = true)) {
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val editor = sharedPref.edit()
            editor.putString("KEY_NAME", fullName)
            editor.putString("KEY_EMAIL", email)
            editor.putString("KEY_PASSWORD", password)
            editor.apply()

            Toast.makeText(this, "Account created! Please login.", Toast.LENGTH_SHORT).show()
            finish()
        }

        tvSignInLink.setOnClickListener {
            finish()
        }
    }
}