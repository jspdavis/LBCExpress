package com.example.lbcexpress2

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class StaffEntry(
    val name: String,
    val email: String,
    val role: String,
    val branch: String,
    val licenseNo: String = "",
    val vehicleType: String = "",
    val plateNo: String = ""
)

object AdminStorage {
    private const val PREFS = "AdminPrefs"
    private const val KEY_STAFF = "KEY_STAFF"

    fun saveStaff(context: Context, entry: StaffEntry) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_STAFF, "[]") ?: "[]"
        val arr = JSONArray(raw)
        arr.put(
            JSONObject().apply {
                put("name", entry.name)
                put("email", entry.email)
                put("role", entry.role)
                put("branch", entry.branch)
                put("licenseNo", entry.licenseNo)
                put("vehicleType", entry.vehicleType)
                put("plateNo", entry.plateNo)
            }
        )
        prefs.edit().putString(KEY_STAFF, arr.toString()).apply()
    }

    fun getStaff(context: Context): List<StaffEntry> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_STAFF, "[]") ?: "[]"
        val arr = JSONArray(raw)
        val out = mutableListOf<StaffEntry>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            out.add(
                StaffEntry(
                    name = o.optString("name"),
                    email = o.optString("email"),
                    role = o.optString("role"),
                    branch = o.optString("branch"),
                    licenseNo = o.optString("licenseNo"),
                    vehicleType = o.optString("vehicleType"),
                    plateNo = o.optString("plateNo")
                )
            )
        }
        return out.reversed()
    }
}
