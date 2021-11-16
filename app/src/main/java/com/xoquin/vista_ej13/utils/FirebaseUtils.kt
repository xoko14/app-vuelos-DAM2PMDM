package com.xoquin.vista_ej13.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class FirebaseUtils {
    companion object{
        fun timestampToDate(timestamp: Timestamp): String {
            val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val netDate = Date(milliseconds)
            return sdf.format(netDate).toString()
        }
    }
}