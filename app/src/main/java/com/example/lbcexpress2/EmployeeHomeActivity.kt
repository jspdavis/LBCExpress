package com.example.lbcexpress2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EmployeeHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employeehomescreen)

        val tvRole = findViewById<TextView>(R.id.tvEmployeeRole)
        val tvEmail = findViewById<TextView>(R.id.tvEmployeeEmail)
        val btnOpenBooking = findViewById<Button>(R.id.btnOpenBooking)
        val btnLogout = findViewById<Button>(R.id.btnLogoutEmployee)

        val role = intent.getStringExtra("EXTRA_ROLE") ?: "Employee"
        val email = intent.getStringExtra("EXTRA_EMAIL") ?: ""
        tvRole.text = role
        tvEmail.text = email

        btnOpenBooking.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnLogout.setOnClickListener {
            val backToLogin = Intent(this, LogInActivity::class.java)
            backToLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(backToLogin)
        }
    }
}
