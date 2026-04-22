package com.example.lbcexpress2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PackageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.packagescreen)

        // 1. Initialize Views
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnBackArrow = findViewById<ImageView>(R.id.btnBackArrow)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val spPackageType = findViewById<Spinner>(R.id.spPackageType)

        // 2. Handle Back Navigation
        btnBack.setOnClickListener { finish() }
        btnBackArrow.setOnClickListener { finish() }

        // 3. Set up Package Type Spinner
        val packageTypes = arrayOf("Select Package Type", "Documents", "Kilobox Mini", "Kilobox Slim", "Kilobox Small")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, packageTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPackageType.adapter = adapter

        // 4. Connect to ConfirmationActivity using Parcelable
        btnNext.setOnClickListener {
            // Validation: Ensure a package type is selected
            if (spPackageType.selectedItemPosition == 0) {
                Toast.makeText(this, "Please select a package type", Toast.LENGTH_SHORT).show()
            } else {
                // 1. Create the Booking object and fill it with data from the previous Intent
                val bookingData = Booking(
                    senderName = intent.getStringExtra("SENDER_NAME") ?: "N/A",
                    senderLocation = intent.getStringExtra("SENDER_LOCATION") ?: "N/A",
                    receiverName = intent.getStringExtra("RECEIVER_NAME") ?: "N/A",
                    receiverBranch = intent.getStringExtra("RECEIVER_BRANCH") ?: "N/A",
                    packageType = spPackageType.selectedItem.toString()
                )

                // 2. Pass the entire object using the key "BOOKING_DATA"
                val nextIntent = Intent(this, ConfirmationActivity::class.java)
                nextIntent.putExtra("BOOKING_DATA", bookingData)

                startActivity(nextIntent)
            }
        }
    }
}