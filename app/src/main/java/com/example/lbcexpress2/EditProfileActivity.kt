package com.example.lbcexpress2

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editprofilescreen)

        // 1. Initialize Views
        val tvHeaderName = findViewById<TextView>(R.id.tvEditProfileName)
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmailUpdate)
        val btnUpdate = findViewById<Button>(R.id.btnUpdateProfile)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        // 2. Load existing data from SharedPreferences
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedFullName = sharedPref.getString("KEY_NAME", "New User") ?: "New User"
        val savedEmail = sharedPref.getString("KEY_EMAIL", "") ?: ""

        // 3. Populate fields
        tvHeaderName.text = savedFullName
        etEmail.setText(savedEmail)

        // Split name logic - handles single names or multiple middle names safely
        val nameParts = savedFullName.trim().split(" ")
        if (nameParts.size >= 2) {
            etFirstName.setText(nameParts[0])
            etLastName.setText(nameParts.subList(1, nameParts.size).joinToString(" "))
        } else {
            etFirstName.setText(savedFullName)
            etLastName.setText("")
        }

        // 4. Back Button Logic
        btnBack.setOnClickListener {
            finish()
        }

        // 5. Update Logic
        btnUpdate.setOnClickListener {
            val fName = etFirstName.text.toString().trim()
            val lName = etLastName.text.toString().trim()
            val newEmail = etEmail.text.toString().trim()

            // Validation
            if (fName.isEmpty() || lName.isEmpty()) {
                Toast.makeText(this, "First and Last name are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newFullName = "$fName $lName"

            // Save to SharedPreferences
            with(sharedPref.edit()) {
                putString("KEY_NAME", newFullName)
                putString("KEY_EMAIL", newEmail)
                apply() // apply() is asynchronous and preferred over commit()
            }

            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()

            // Optional: If you want the header to update immediately before closing
            tvHeaderName.text = newFullName

            finish()
        }
    }
}