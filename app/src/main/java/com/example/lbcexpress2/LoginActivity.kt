package com.example.lbcexpress2

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginscreen)

        val etIdentifier = findViewById<EditText>(R.id.etIdentifier)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)
        val tvIdentifierLabel = findViewById<TextView>(R.id.tvIdentifierLabel)
        val rgRole = findViewById<RadioGroup>(R.id.rgRole)
        val rbEmployee = findViewById<RadioButton>(R.id.rbEmployee)

        rgRole.setOnCheckedChangeListener { _, checkedId ->
            val isEmployee = rgRole.checkedRadioButtonId == R.id.rbEmployee
            tvIdentifierLabel.text = if (isEmployee) "Employee Email" else "Customer Email"
            etIdentifier.hint = if (isEmployee) "example@lbc.com" else "Enter your registered email"
            tvSignUp.visibility = if (isEmployee) TextView.GONE else TextView.VISIBLE
        }
        rgRole.check(R.id.rbCustomer)

        btnSignIn.setOnClickListener {
            val email = etIdentifier.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validate(email, password, etIdentifier, etPassword)) {
                val isEmployee = rgRole.checkedRadioButtonId == R.id.rbEmployee
                android.util.Log.d("LOGIN_DEBUG", "Checked ID: ${rgRole.checkedRadioButtonId}, Expected ID: ${R.id.rbEmployee}")
                if (isEmployee) {
                    val role = resolveEmployeeRole(email, password)
                    if (role != null) {
                        val destination = if (role == "Admin") {
                            AdminDashboardActivity::class.java
                        } else {
                            EmployeeHomeActivity::class.java
                        }
                        val intent = Intent(this, destination).apply {
                            putExtra("EXTRA_ROLE", role)
                            putExtra("EXTRA_EMAIL", email)
                        }
                        startActivity(intent)
                        return@setOnClickListener
                    }
                    Toast.makeText(this, "Invalid employee credentials.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val savedEmail = prefs.getString("KEY_EMAIL", null)
                val savedPassword = prefs.getString("KEY_PASSWORD", null)
                if (savedEmail == null || savedPassword == null) {
                    Toast.makeText(this, "No customer account found. Please sign up first.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (email.equals(savedEmail, ignoreCase = true) && password == savedPassword) {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("EXTRA_EMAIL", email)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid customer credentials.", Toast.LENGTH_SHORT).show()
                }
            }
        }

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

    private fun resolveEmployeeRole(email: String, password: String): String? {
        val employeeAccounts = mapOf(
            "admin@lbc.com" to Pair("admin123", "Admin"),
            "branch@lbc.com" to Pair("branch123", "Branch Staff"),
            "rider@lbc.com" to Pair("rider123", "Rider")
        )
        val account = employeeAccounts[email.lowercase()] ?: return null
        return if (account.first == password) account.second else null
    }
}