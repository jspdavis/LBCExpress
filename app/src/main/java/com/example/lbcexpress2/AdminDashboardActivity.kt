package com.example.lbcexpress2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var sectionStaff: LinearLayout
    private lateinit var sectionRiders: LinearLayout
    private lateinit var sectionShipments: LinearLayout
    private lateinit var containerStaff: LinearLayout
    private lateinit var containerRiders: LinearLayout
    private lateinit var containerShipments: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admindashboardscreen)

        val tvAdmin = findViewById<TextView>(R.id.tvAdminName)
        val btnLogout = findViewById<Button>(R.id.btnLogoutAdmin)
        val btnTabStaff = findViewById<Button>(R.id.btnTabStaff)
        val btnTabRiders = findViewById<Button>(R.id.btnTabRiders)
        val btnTabShipments = findViewById<Button>(R.id.btnTabShipments)
        val btnCreate = findViewById<Button>(R.id.btnCreateAccount)

        val etName = findViewById<EditText>(R.id.etStaffName)
        val etEmail = findViewById<EditText>(R.id.etStaffEmail)
        val etPassword = findViewById<EditText>(R.id.etStaffPassword)
        val spRole = findViewById<Spinner>(R.id.spStaffRole)
        val spBranch = findViewById<Spinner>(R.id.spStaffBranch)
        val riderFields = findViewById<LinearLayout>(R.id.layoutRiderFields)
        val etLicense = findViewById<EditText>(R.id.etLicenseNo)
        val spVehicle = findViewById<Spinner>(R.id.spVehicleType)
        val etPlate = findViewById<EditText>(R.id.etPlateNo)

        sectionStaff = findViewById(R.id.sectionStaff)
        sectionRiders = findViewById(R.id.sectionRiders)
        sectionShipments = findViewById(R.id.sectionShipments)
        containerStaff = findViewById(R.id.containerStaffDirectory)
        containerRiders = findViewById(R.id.containerRiderDirectory)
        containerShipments = findViewById(R.id.containerShipmentTracking)

        val email = intent.getStringExtra("EXTRA_EMAIL") ?: "admin@lbc.com"
        tvAdmin.text = "Admin: $email"

        spRole.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Staff", "Branch Staff", "Rider", "Admin")
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        spBranch.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Cebu City - LBC Ayala Center", "Makati - LBC Greenbelt", "Mandaue - LBC JCenter")
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        spVehicle.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Motorcycle", "Bicycle", "Van", "Truck")
        ).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        spRole.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selected = spRole.selectedItem.toString()
                riderFields.visibility =
                    if (selected == "Rider") android.view.View.VISIBLE else android.view.View.GONE
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }

        btnCreate.setOnClickListener {
            val name = etName.text.toString().trim()
            val emailInput = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val role = spRole.selectedItem.toString()
            val branch = spBranch.selectedItem.toString()

            if (name.isBlank() || emailInput.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Complete all required staff fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val staff = StaffEntry(
                name = name,
                email = emailInput,
                role = role,
                branch = branch,
                licenseNo = etLicense.text.toString().trim(),
                vehicleType = spVehicle.selectedItem.toString(),
                plateNo = etPlate.text.toString().trim()
            )
            AdminStorage.saveStaff(this, staff)
            Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()

            etName.text.clear()
            etEmail.text.clear()
            etPassword.text.clear()
            etLicense.text.clear()
            etPlate.text.clear()
            renderDirectories()
        }

        btnTabStaff.setOnClickListener { showSection("staff") }
        btnTabRiders.setOnClickListener { showSection("riders") }
        btnTabShipments.setOnClickListener { showSection("shipments") }
        btnLogout.setOnClickListener {
            val i = Intent(this, LogInActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

        showSection("staff")
    }

    override fun onResume() {
        super.onResume()
        renderDirectories()
    }

    private fun showSection(which: String) {
        sectionStaff.visibility = if (which == "staff") android.view.View.VISIBLE else android.view.View.GONE
        sectionRiders.visibility = if (which == "riders") android.view.View.VISIBLE else android.view.View.GONE
        sectionShipments.visibility = if (which == "shipments") android.view.View.VISIBLE else android.view.View.GONE
        renderDirectories()
    }

    private fun renderDirectories() {
        val staffList = AdminStorage.getStaff(this) ?: emptyList()
        containerStaff.removeAllViews()
        containerRiders.removeAllViews()
        containerShipments.removeAllViews()

        val staffOnly = staffList.filter { it.role != "Rider" }
        val ridersOnly = staffList.filter { it.role == "Rider" }

        if (staffOnly.isEmpty()) {
            containerStaff.addView(simpleText("No staff registered yet."))
        } else {
            staffOnly.forEach {
                containerStaff.addView(simpleText("${it.name} | ${it.role} | ${it.branch}"))
            }
        }

        if (ridersOnly.isEmpty()) {
            containerRiders.addView(simpleText("No riders registered yet."))
        } else {
            ridersOnly.forEach {
                val details = "${it.name} | ${it.branch} | ${it.licenseNo.ifBlank { "No License" }} | ${it.vehicleType} | ${it.plateNo.ifBlank { "No Plate" }}"
                containerRiders.addView(simpleText(details))
            }
        }

        val shipments = BookingStorage.getBookings(this) ?: emptyList()
        if (shipments.isEmpty()) {
            containerShipments.addView(simpleText("No active shipments found."))
        } else {
            shipments.forEach {
                containerShipments.addView(simpleText("${it.tracking} | ${it.status} | ${it.route}"))
            }
        }
    }

    private fun simpleText(value: String): TextView =
        TextView(this).apply {
            text = value
            textSize = 13f
            setPadding(8, 8, 8, 8)
        }
}
