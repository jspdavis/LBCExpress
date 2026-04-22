package com.example.lbcexpress2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    var senderName: String = "",
    var senderLocation: String = "",
    var receiverName: String = "",
    var receiverBranch: String = "",
    var packageType: String = ""
) : Parcelable