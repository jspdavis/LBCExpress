package com.example.lbcexpress2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
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
        val etItemName = findViewById<EditText>(R.id.etItemName)
        val etItemValue = findViewById<EditText>(R.id.etItemValue)
        val spPackageType = findViewById<Spinner>(R.id.spPackageType)
        val rbRush = findViewById<RadioButton>(R.id.rbRush)
        val rbCopYes = findViewById<RadioButton>(R.id.rbCopYes)

        // 2. Handle Back Navigation
        btnBack.setOnClickListener { finish() }
        btnBackArrow.setOnClickListener { finish() }

        // 3. Set up Package Type Spinner
        val packageTypes = arrayOf(
            "Select Type",
            "N-Pouch Regular (up to 0.5 kg)",
            "N-Pouch XL (up to 1 kg)",
            "N-Pack Small (up to 1 kg)",
            "N-Pack Large (up to 3 kg)",
            "Kilobox Mini (up to 1 kg)",
            "Kilobox Small (up to 3 kg)",
            "Kilobox Large (up to 10 kg)",
            "Own Box / Own Packaging"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, packageTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPackageType.adapter = adapter

        btnNext.setOnClickListener {
            val itemName = etItemName.text.toString().trim()
            val itemValueText = etItemValue.text.toString().trim()
            val pkgType = spPackageType.selectedItem.toString()
            if (itemName.isBlank() || itemValueText.isBlank() || pkgType == "Select Type") {
                Toast.makeText(this, "Please complete package details.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val itemValue = itemValueText.toDoubleOrNull()
            if (itemValue == null || itemValue <= 0.0) {
                Toast.makeText(this, "Enter a valid item value.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bookingData = intent.getParcelableExtra<Booking>("BOOKING_DATA") ?: Booking()
            bookingData.itemName = itemName
            bookingData.itemValue = itemValue
            bookingData.packageType = pkgType
            bookingData.leadTime = if (rbRush.isChecked) "Rush" else "Regular"
            bookingData.paymentCollection = if (rbCopYes.isChecked) "Yes" else "No"

            val nextIntent = Intent(this, ConfirmationActivity::class.java)
            nextIntent.putExtra("BOOKING_DATA", bookingData)
            startActivity(nextIntent)
        }
    }
}