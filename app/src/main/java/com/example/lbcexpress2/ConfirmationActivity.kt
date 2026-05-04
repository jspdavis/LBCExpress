package com.example.lbcexpress2

import android.content.Intent
import android.os.Bundle
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

        val tvSenderName = findViewById<TextView>(R.id.tvConfirmSenderName)
        val tvSenderPhone = findViewById<TextView>(R.id.tvConfirmSenderPhone)
        val tvSenderLoc = findViewById<TextView>(R.id.tvConfirmSenderLoc)
        val tvSenderBranch = findViewById<TextView>(R.id.tvConfirmSenderBranch)
        val tvReceiverName = findViewById<TextView>(R.id.tvConfirmReceiverName)
        val tvReceiverPhone = findViewById<TextView>(R.id.tvConfirmReceiverPhone)
        val tvReceiverLocation = findViewById<TextView>(R.id.tvConfirmReceiverLocation)
        val tvReceiverBranch = findViewById<TextView>(R.id.tvConfirmReceiverBranch)
        val tvDeliveryMethod = findViewById<TextView>(R.id.tvConfirmDeliveryMethod)
        val tvItemName = findViewById<TextView>(R.id.tvConfirmItemName)
        val tvItemValue = findViewById<TextView>(R.id.tvConfirmItemValue)
        val tvPackageType = findViewById<TextView>(R.id.tvConfirmPackageType)
        val tvLeadTime = findViewById<TextView>(R.id.tvConfirmLeadTime)
        val tvPaymentCollection = findViewById<TextView>(R.id.tvConfirmPaymentCollection)
        val tvShippingFee = findViewById<TextView>(R.id.tvShippingFee)
        val tvValuationFee = findViewById<TextView>(R.id.tvValuationFee)
        val tvTotalFee = findViewById<TextView>(R.id.tvTotalFee)

        val cbTerms = findViewById<CheckBox>(R.id.cbTerms)
        val cbDeclare = findViewById<CheckBox>(R.id.cbDeclare)
        val btnBookNow = findViewById<Button>(R.id.btnBookNow)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnBackArrow = findViewById<ImageView>(R.id.btnBackArrow)

        val booking = intent.getParcelableExtra<Booking>("BOOKING_DATA")
        if (booking != null) {
            tvSenderName.text = booking.senderName
            tvSenderPhone.text = booking.senderPhone
            tvSenderLoc.text = "${booking.senderProvince}, ${booking.senderCity}"
            tvSenderBranch.text = booking.senderDropOffBranch
            tvReceiverName.text = booking.receiverName
            tvReceiverPhone.text = booking.receiverPhone
            tvDeliveryMethod.text = booking.deliveryMethod
            tvReceiverLocation.text = "${booking.receiverStreet} ${booking.receiverCity}, ${booking.receiverProvince}".trim()
            tvReceiverBranch.text = booking.receiverBranch
            tvItemName.text = booking.itemName
            tvItemValue.text = "PHP %.2f".format(booking.itemValue)
            tvPackageType.text = booking.packageType
            tvLeadTime.text = booking.leadTime
            tvPaymentCollection.text = booking.paymentCollection

            val shippingFee = 155.0
            val valuationFee = booking.itemValue * 0.015
            val totalFee = shippingFee + valuationFee
            tvShippingFee.text = "PHP %.2f".format(shippingFee)
            tvValuationFee.text = "PHP %.2f".format(valuationFee)
            tvTotalFee.text = "PHP %.2f".format(totalFee)
        } else {
            Toast.makeText(this, "Error: Booking data not found", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener { finish() }
        btnBackArrow.setOnClickListener { finish() }

        val checker = {
            btnBookNow.isEnabled = cbTerms.isChecked && cbDeclare.isChecked
        }
        cbTerms.setOnCheckedChangeListener { _, _ -> checker() }
        cbDeclare.setOnCheckedChangeListener { _, _ -> checker() }
        checker()

        btnBookNow.setOnClickListener {
            booking?.let { BookingStorage.saveBooking(this, it) }
            Toast.makeText(this, "Booking Successful!", Toast.LENGTH_LONG).show()

            val homeIntent = Intent(this, MainActivity::class.java)
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(homeIntent)
            finish()
        }
    }
}