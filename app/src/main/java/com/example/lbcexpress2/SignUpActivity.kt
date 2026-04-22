package com.example.lbcexpress2

import android.os.Bundle
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
        val etLast = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmailSignUp)
        val btnSignUp = findViewById<Button>(R.id.btnSignUpSubmit)
        val tvSignInLink = findViewById<TextView>(R.id.tvSignInLink)

        // Inside SignUpActivity.kt - btnSignUp.setOnClickListener
        btnSignUp.setOnClickListener {
            val fName = etFirst.text.toString().trim()
            val lName = etLast.text.toString().trim()
            val fullName = "$fName $lName"

            if (fName.isNotEmpty() && lName.isNotEmpty()) {
                // 1. Get the SharedPreferences editor
                val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPref.edit()

                // 2. Save the full name under the key "KEY_NAME"
                editor.putString("KEY_NAME", fullName)
                editor.apply()

                // 3. Move to the next screen or close
                Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Return to Login screen
        tvSignInLink.setOnClickListener {
            finish()
        }
    }
}