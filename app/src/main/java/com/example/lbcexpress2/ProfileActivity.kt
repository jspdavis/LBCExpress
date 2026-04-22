package com.example.lbcexpress2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profilescreen)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        val btnLogout = findViewById<RelativeLayout>(R.id.btnLogout)
        val itemProfile = findViewById<RelativeLayout>(R.id.itemProfile)

        // 1. Launch EditProfileActivity
        // Simplified: SharedPreferences in onResume() handles the data update
        itemProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        // 2. Logout Logic
        btnLogout.setOnClickListener {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LogInActivity::class.java)
            // Clears the activity stack so user cannot go back to profile
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // 3. Bottom Navigation Logic
        bottomNav.selectedItemId = R.id.nav_profile
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Close profile so it doesn't stay in the stack
                    true
                }
                R.id.nav_calculator -> {
                    Toast.makeText(this, "Calculator Selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_location -> {
                    Toast.makeText(this, "Location Selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }
    }

    // 4. This runs every time the Activity becomes visible again
    override fun onResume() {
        super.onResume()

        // Sync with the same "UserPrefs" used in EditProfileActivity
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedName = sharedPref.getString("KEY_NAME", "New User")

        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        tvUserName.text = savedName
    }
}