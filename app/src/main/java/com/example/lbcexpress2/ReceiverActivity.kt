package com.example.lbcexpress2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receiverscreen)

        // 1. Initialize Views
        val spProvince = findViewById<Spinner>(R.id.spReceiverProvince)
        val spCity = findViewById<Spinner>(R.id.spReceiverCity)
        val spBranch = findViewById<Spinner>(R.id.spReceiverBranch)
        val etRecFirstName = findViewById<EditText>(R.id.etReceiverFirst)
        val etRecLastName = findViewById<EditText>(R.id.etReceiverLast)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnNextReceiver = findViewById<Button>(R.id.btnNextReceiver)

        btnBack.setOnClickListener { finish() }

        // 2. Data for Provinces
        val provinces = arrayOf("SELECT PROVINCE", "CEBU", "METRO MANILA")
        val provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spProvince.adapter = provinceAdapter

        // 3. Province Selection Logic
        spProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedProvince = provinces[position]
                val cities = when (selectedProvince) {
                    "CEBU" -> arrayOf("MANDAUE CITY", "CEBU CITY")
                    "METRO MANILA" -> arrayOf("MAKATI", "PASIG")
                    else -> arrayOf("Select Province First")
                }
                val cityAdapter = ArrayAdapter(this@ReceiverActivity, android.R.layout.simple_spinner_item, cities)
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spCity.adapter = cityAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 4. City Selection Logic (Updates the Branch Spinner)
        spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = spCity.selectedItem.toString()
                val branches = when (selectedCity) {
                    "MANDAUE CITY" -> arrayOf("LBC JCenter Mall", "LBC Insular Square", "LBC Mandaue Centro")
                    "CEBU CITY" -> arrayOf("LBC SM City Cebu", "LBC Ayala Center", "LBC Colon")
                    "MAKATI" -> arrayOf("LBC Greenbelt", "LBC Glorietta", "LBC Chino Roces")
                    "PASIG" -> arrayOf("LBC Estancia", "LBC SM East Ortigas")
                    else -> arrayOf("Select City First")
                }
                val branchAdapter = ArrayAdapter(this@ReceiverActivity, android.R.layout.simple_spinner_item, branches)
                branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spBranch.adapter = branchAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // 5. Connect to PackageActivity
        btnNextReceiver.setOnClickListener {
            // Simple Validation
            if (etRecFirstName.text.isBlank() || etRecLastName.text.isBlank() || spProvince.selectedItemPosition == 0) {
                Toast.makeText(this, "Please complete all receiver details", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PackageActivity::class.java)

                // CARRY FORWARD: Sender data from previous intent
                intent.putExtra("SENDER_NAME", getIntent().getStringExtra("SENDER_NAME"))
                intent.putExtra("SENDER_LOCATION", getIntent().getStringExtra("SENDER_LOCATION"))

                // ADD NEW: Receiver input data
                val receiverFullName = "${etRecFirstName.text} ${etRecLastName.text}"
                intent.putExtra("RECEIVER_NAME", receiverFullName)
                intent.putExtra("RECEIVER_BRANCH", spBranch.selectedItem.toString())

                startActivity(intent)
            }
        }
    }
}