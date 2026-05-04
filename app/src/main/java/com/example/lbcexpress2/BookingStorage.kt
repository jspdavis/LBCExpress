package com.example.lbcexpress2

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class BookingHistoryEntry(
    val tracking: String,
    val status: String,
    val packageType: String,
    val route: String
)

object BookingStorage {
    private const val PREFS = "BookingPrefs"
    private const val KEY_BOOKINGS = "KEY_BOOKINGS"

    fun saveBooking(context: Context, booking: Booking) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val current = prefs.getString(KEY_BOOKINGS, "[]") ?: "[]"
        val array = JSONArray(current)

        val tracking = "LBC${System.currentTimeMillis().toString().takeLast(9)}"
        val route = "${booking.senderCity} -> ${booking.receiverCity}"

        val entry = JSONObject().apply {
            put("tracking", tracking)
            put("status", "Pending")
            put("packageType", booking.packageType)
            put("route", route)
        }
        array.put(entry)
        prefs.edit().putString(KEY_BOOKINGS, array.toString()).apply()
    }

    fun getBookings(context: Context): List<BookingHistoryEntry> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_BOOKINGS, "[]") ?: "[]"
        val array = JSONArray(raw)
        val items = mutableListOf<BookingHistoryEntry>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            items.add(
                BookingHistoryEntry(
                    tracking = obj.optString("tracking", "Not yet assigned"),
                    status = obj.optString("status", "Pending"),
                    packageType = obj.optString("packageType", "Package"),
                    route = obj.optString("route", "Route unavailable")
                )
            )
        }
        return items.reversed()
    }
}
