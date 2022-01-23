package com.testing.slotrooms.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Long?.dateFormat(template: String = "dd MMM yyyy"): String {
    this?.let{
        val formatter = SimpleDateFormat(template, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = it
        return formatter.format(calendar.time)
    }
    return ""
}

fun Long.timeFormat(template: String = "HH : mm") : String {
    return SimpleDateFormat(template, Locale.getDefault()).format(Date(this))
}

