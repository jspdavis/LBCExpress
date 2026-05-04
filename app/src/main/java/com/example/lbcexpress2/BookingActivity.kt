package com.example.lbcexpress2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    var senderName: String = "",
    var senderPhone: String = "",
    var senderProvince: String = "",
    var senderCity: String = "",
    var senderDropOffBranch: String = "",
    var receiverName: String = "",
    var receiverPhone: String = "",
    var deliveryMethod: String = "Branch Pick Up",
    var receiverProvince: String = "",
    var receiverCity: String = "",
    var receiverBranch: String = "",
    var receiverStreet: String = "",
    var itemName: String = "",
    var itemValue: Double = 0.0,
    var packageType: String = "",
    var leadTime: String = "Rush",
    var paymentCollection: String = "No"
) : Parcelable