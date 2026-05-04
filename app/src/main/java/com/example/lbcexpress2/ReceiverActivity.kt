package com.example.lbcexpress2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.receiverscreen)

        val spProvince = findViewById<Spinner>(R.id.spReceiverProvince)
        val spCity = findViewById<Spinner>(R.id.spReceiverCity)
        val spBranch = findViewById<Spinner>(R.id.spReceiverBranch)
        val spRiderProvince = findViewById<Spinner>(R.id.spRiderProvince)
        val spRiderCity = findViewById<Spinner>(R.id.spRiderCity)
        val etStreet = findViewById<EditText>(R.id.etRiderStreet)
        val etRecFirstName = findViewById<EditText>(R.id.etReceiverFirst)
        val etRecLastName = findViewById<EditText>(R.id.etReceiverLast)
        val etRecPhone = findViewById<EditText>(R.id.etReceiverPhone)
        val rbBranch = findViewById<RadioButton>(R.id.rbBranchPickUp)
        val rbRider = findViewById<RadioButton>(R.id.rbRiderDelivery)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnNextReceiver = findViewById<Button>(R.id.btnNextReceiver)
        val sectionBranch = findViewById<android.view.View>(R.id.layoutBranchSection)
        val sectionRider = findViewById<android.view.View>(R.id.layoutRiderSection)

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
        spRiderProvince.adapter = provinceAdapter

        rbBranch.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                sectionBranch.visibility = android.view.View.VISIBLE
                sectionRider.visibility = android.view.View.GONE
            }
        }
        rbRider.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                sectionBranch.visibility = android.view.View.GONE
                sectionRider.visibility = android.view.View.VISIBLE
            }
        }

        spProvince.setOnItemSelectedListener {
            val selectedProvince = spProvince.selectedItem.toString()
            val cities = if (selectedProvince == "Select Province") {
                listOf("Select City")
            } else {
                listOf("Select City") + (branchData[selectedProvince]?.keys?.sorted().orEmpty())
            }
            spCity.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities).apply {
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
            spBranch.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, branches).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }

        spRiderProvince.setOnItemSelectedListener {
            val selectedProvince = spRiderProvince.selectedItem.toString()
            val cities = if (selectedProvince == "Select Province") {
                listOf("Select City")
            } else {
                listOf("Select City") + (branchData[selectedProvince]?.keys?.sorted().orEmpty())
            }
            spRiderCity.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }

        btnNextReceiver.setOnClickListener {
            val booking = intent.getParcelableExtra<Booking>("BOOKING_DATA") ?: Booking()
            val first = etRecFirstName.text.toString().trim()
            val last = etRecLastName.text.toString().trim()
            val phone = etRecPhone.text.toString().trim()
            if (first.isBlank() || last.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "Please complete receiver details.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            booking.receiverName = "$first $last"
            booking.receiverPhone = phone
            booking.deliveryMethod = if (rbRider.isChecked) "Rider Delivery" else "Branch Pick Up"

            if (booking.deliveryMethod == "Branch Pick Up") {
                val province = spProvince.selectedItem.toString()
                val city = spCity.selectedItem.toString()
                val branch = spBranch.selectedItem.toString()
                if (province == "Select Province" || city == "Select City" || branch == "Select Branch") {
                    Toast.makeText(this, "Complete branch pickup details.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                booking.receiverProvince = province
                booking.receiverCity = city
                booking.receiverBranch = branch
                booking.receiverStreet = ""
            } else {
                val province = spRiderProvince.selectedItem.toString()
                val city = spRiderCity.selectedItem.toString()
                val street = etStreet.text.toString().trim()
                if (province == "Select Province" || city == "Select City" || street.isBlank()) {
                    Toast.makeText(this, "Complete rider delivery address.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                booking.receiverProvince = province
                booking.receiverCity = city
                booking.receiverStreet = street
                booking.receiverBranch = ""
            }

            val next = Intent(this, PackageActivity::class.java)
            next.putExtra("BOOKING_DATA", booking)
            startActivity(next)
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