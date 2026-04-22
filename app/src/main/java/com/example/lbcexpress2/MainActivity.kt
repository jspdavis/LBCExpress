package com.example.lbcexpress2 // Double-check this matches your actual package name

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize Navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.nav_home

        // 2. Initialize Main Buttons
        val btnBranchDropOff = findViewById<LinearLayout>(R.id.btnBranchDropOff)
        val btnSchedulePickUp = findViewById<LinearLayout>(R.id.btnSchedulePickUp)

        // --- NAVIGATION MENU LOGIC ---
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_calculator -> {
                    Toast.makeText(this, "Calculator Selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_location -> {
                    Toast.makeText(this, "Location Selected", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // --- BUTTON CLICK LOGIC ---

        // Connect Branch Drop Off to SenderActivity
        btnBranchDropOff.setOnClickListener {
            val intent = Intent(this, SenderActivity::class.java)
            startActivity(intent)
        }

        // Connect Schedule Pick Up (Optional but recommended)
        btnSchedulePickUp.setOnClickListener {
            // Usually goes to the same SenderActivity to start the booking process
            val intent = Intent(this, SenderActivity::class.java)
            startActivity(intent)
        }
    }
}