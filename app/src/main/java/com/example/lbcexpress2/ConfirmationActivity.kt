package com.example.lbcexpress2

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmationscreen)

        // 1. Initialize TextViews for Data Display
        val tvSenderName = findViewById<TextView>(R.id.tvConfirmSenderName)
        val tvSenderLoc = findViewById<TextView>(R.id.tvConfirmSenderLoc)
        val tvReceiverName = findViewById<TextView>(R.id.tvConfirmReceiverName)
        val tvReceiverBranch = findViewById<TextView>(R.id.tvConfirmReceiverBranch)
        val tvPackageType = findViewById<TextView>(R.id.tvConfirmPackageType)

        // 2. Initialize Interactive Views
        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val cbDeclare = findViewById<CheckBox>(R.id.cbDeclare)
        val btnBookNow = findViewById<Button>(R.id.btnBookNow)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnBackArrow = findViewById<ImageView>(R.id.btnBackArrow)

        // 3. Populate data using the Parcelable Booking object
        // For SDK 33+, use getParcelableExtra(key, class). For older, use the simplified version.
        val booking = intent.getParcelableExtra<Booking>("BOOKING_DATA")

        if (booking != null) {
            tvSenderName.text = booking.senderName
            tvSenderLoc.text = booking.senderLocation
            tvReceiverName.text = booking.receiverName
            tvReceiverBranch.text = booking.receiverBranch
            tvPackageType.text = booking.packageType
        } else {
            // Optional: Handle case where data didn't transfer
            Toast.makeText(this, "Error: Booking data not found", Toast.LENGTH_SHORT).show()
        }

        // 4. Back Navigation
        btnBack.setOnClickListener { finish() }
        btnBackArrow.setOnClickListener { finish() }

        // 5. Checkbox Logic
        val checkListener = View.OnClickListener {
            val isReady = cbTerms.isChecked && cbDeclare.isChecked
            btnBookNow.isEnabled = isReady
            btnBookNow.backgroundTintList = ColorStateList.valueOf(
                Color.parseColor(if (isReady) "#D81B60" else "#BCBCBC")
            )
        }

        cbTerms.setOnClickListener(checkListener)
        cbDeclare.setOnClickListener(checkListener)

        // 6. Final Booking Logic
        btnBookNow.setOnClickListener {
            Toast.makeText(this, "Booking Successful!", Toast.LENGTH_LONG).show()

            val homeIntent = Intent(this, MainActivity::class.java)
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(homeIntent)
            finish()
        }
    }
}