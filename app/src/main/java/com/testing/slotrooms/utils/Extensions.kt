package com.testing.slotrooms.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long?.dateFormat(template: String): String {
    this?.let{
        val formatter = SimpleDateFormat(template)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = it
        return formatter.format(calendar.time)
    }
    return ""
}