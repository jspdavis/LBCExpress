package com.example.lbcexpress2 // Double-check this matches your actual package name

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var historyContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcomeCustomer)
        val etTrack = findViewById<EditText>(R.id.etTrackNumber)
        val btnTrack = findViewById<Button>(R.id.btnTrackNow)
        val btnBookNow = findViewById<Button>(R.id.btnBookNow)
        val btnLogout = findViewById<Button>(R.id.btnLogoutCustomer)
        historyContainer = findViewById(R.id.bookingHistoryContainer)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val name = prefs.getString("KEY_NAME", "Customer") ?: "Customer"
        tvWelcome.text = "Welcome, $name"

        btnTrack.setOnClickListener {
            val tracking = etTrack.text.toString().trim()
            if (tracking.isBlank()) {
                etTrack.error = "Enter tracking number"
                return@setOnClickListener
            }
            Toast.makeText(this, "Tracking: $tracking", Toast.LENGTH_SHORT).show()
        }

        btnBookNow.setOnClickListener {
            startActivity(Intent(this, SenderActivity::class.java))
        }

        btnLogout.setOnClickListener {
            val backToLogin = Intent(this, LogInActivity::class.java)
            backToLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(backToLogin)
        }
        renderBookingHistory()
    }

    override fun onResume() {
        super.onResume()
        renderBookingHistory()
    }

    private fun addBookingCard(
        container: LinearLayout,
        tracking: String,
        status: String,
        packageType: String,
        route: String
    ) {
        val card = layoutInflater.inflate(R.layout.item_booking_history, container, false)
        card.findViewById<TextView>(R.id.tvItemTracking).text = tracking
        card.findViewById<TextView>(R.id.tvItemStatus).text = status
        card.findViewById<TextView>(R.id.tvItemPackage).text = packageType
        card.findViewById<TextView>(R.id.tvItemRoute).text = route
        container.addView(card)
    }

    private fun renderBookingHistory() {
        historyContainer.removeAllViews()
        val bookings = BookingStorage.getBookings(this)

        if (bookings.isEmpty()) {
            val empty = TextView(this).apply {
                text = "No bookings yet. Ready to ship? Book a package."
                textSize = 13f
            }
            historyContainer.addView(empty)
            return
        }

        bookings.forEach { booking ->
            addBookingCard(
                historyContainer,
                booking.tracking,
                booking.status,
                booking.packageType,
                booking.route
            )
        }
    }
}