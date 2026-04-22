package com.example.lbcexpress2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SenderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.senderscreen)

        // 1. Initialize Views
        val spProvince = findViewById<Spinner>(R.id.spProvince)
        val spCity = findViewById<Spinner>(R.id.spCity)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val etSenderFirstName = findViewById<EditText>(R.id.etFirstName)
        val etSenderLastName = findViewById<EditText>(R.id.etLastName)
        val etSenderMobile = findViewById<EditText>(R.id.etMobile)

        // 2. Load Saved Profile Data (Optional Quality of Life Feature)
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedName = sharedPref.getString("KEY_NAME", "")
        if (!savedName.isNullOrBlank()) {
            val nameParts = savedName.split(" ")
            etSenderFirstName.setText(nameParts[0])
            if (nameParts.size > 1) {
                etSenderLastName.setText(nameParts.subList(1, nameParts.size).joinToString(" "))
            }
        }

        btnBack.setOnClickListener { finish() }

        // 3. Set up Province Spinner
        val provinces = arrayOf("SELECT PROVINCE", "CEBU", "METRO MANILA", "DAVAO")
        val provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProvince.adapter = provinceAdapter

        // 4. Province and City Logic
        spProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedProvince = provinces[position]
                val cities = when (selectedProvince) {
                    "CEBU" -> arrayOf("CITY OF MANDAUE", "CEBU CITY", "LAPU-LAPU CITY")
                    "METRO MANILA" -> arrayOf("MAKATI", "QUEZON CITY", "MANILA")
                    "DAVAO" -> arrayOf("DAVAO CITY", "DIGOS", "TAGUM")
                    else -> arrayOf("Select a province first")
                }
                val cityAdapter = ArrayAdapter(this@SenderActivity, android.R.layout.simple_spinner_item, cities)
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spCity.adapter = cityAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 5. Connect to ReceiverActivity
        btnNext.setOnClickListener {
            val firstName = etSenderFirstName.text.toString().trim()
            val lastName = etSenderLastName.text.toString().trim()

            // Validation
            if (firstName.isEmpty() || lastName.isEmpty() || spProvince.selectedItemPosition == 0) {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ReceiverActivity::class.java)

                // Capture user input
                val fullName = "$firstName $lastName"
                val location = "${spProvince.selectedItem}, ${spCity.selectedItem}"

                // Pack data into the intent
                intent.putExtra("SENDER_NAME", fullName)
                intent.putExtra("SENDER_MOBILE", etSenderMobile.text.toString())
                intent.putExtra("SENDER_LOCATION", location)

                startActivity(intent)
            }
        }
    }
}