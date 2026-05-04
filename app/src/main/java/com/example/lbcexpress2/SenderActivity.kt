package com.example.lbcexpress2

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

        val spProvince = findViewById<Spinner>(R.id.spProvince)
        val spCity = findViewById<Spinner>(R.id.spCity)
        val spBranch = findViewById<Spinner>(R.id.spDropBranch)
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val etSenderFirstName = findViewById<EditText>(R.id.etFirstName)
        val etSenderLastName = findViewById<EditText>(R.id.etLastName)
        val etSenderMobile = findViewById<EditText>(R.id.etMobile)

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

        val branchData = mapOf(
            "CEBU" to mapOf(
                "MANDAUE CITY" to listOf("LBC JCenter Mall", "LBC Insular Square", "LBC Mandaue Centro"),
                "CEBU CITY" to listOf("LBC SM City Cebu", "LBC Ayala Center", "LBC Colon")
            ),
            "METRO MANILA" to mapOf(
                "MAKATI" to listOf("LBC Greenbelt", "LBC Glorietta", "LBC Chino Roces"),
                "PASIG" to listOf("LBC Estancia", "LBC SM East Ortigas")
            ),
            "DAVAO" to mapOf(
                "DAVAO CITY" to listOf("LBC Gaisano Davao", "LBC SM Davao")
            )
        )

        val provinces = listOf("Select Province") + branchData.keys.sorted()
        val provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProvince.adapter = provinceAdapter

        spProvince.setOnItemSelectedListener {
            val selectedProvince = spProvince.selectedItem.toString()
            val cities = if (selectedProvince == "Select Province") {
                listOf("Select City")
            } else {
                listOf("Select City") + (branchData[selectedProvince]?.keys?.sorted().orEmpty())
            }
            spCity.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, cities).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }

            spBranch.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Select Branch")).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
        }

        spCity.setOnItemSelectedListener {
            val selectedProvince = spProvince.selectedItem.toString()
            val selectedCity = spCity.selectedItem.toString()
            val branches = if (selectedCity == "Select City") {
                listOf("Select Branch")
            } else {
                listOf("Select Branch") + (branchData[selectedProvince]?.get(selectedCity).orEmpty())
            }
            spBranch.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, branches).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
        }

        btnNext.setOnClickListener {
            val firstName = etSenderFirstName.text.toString().trim()
            val lastName = etSenderLastName.text.toString().trim()
            val phone = etSenderMobile.text.toString().trim()
            val province = spProvince.selectedItem.toString()
            val city = spCity.selectedItem.toString()
            val branch = spBranch.selectedItem.toString()

            if (
                firstName.isBlank() || lastName.isBlank() || phone.isBlank() ||
                province == "Select Province" || city == "Select City" || branch == "Select Branch"
            ) {
                Toast.makeText(this, "Please complete sender details.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val booking = Booking(
                senderName = "$firstName $lastName",
                senderPhone = phone,
                senderProvince = province,
                senderCity = city,
                senderDropOffBranch = branch
            )

            val intent = Intent(this, ReceiverActivity::class.java)
            intent.putExtra("BOOKING_DATA", booking)
            startActivity(intent)
        }
    }

    private fun Spinner.setOnItemSelectedListener(onSelect: () -> Unit) {
        onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) = onSelect()

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
        }
    }
}