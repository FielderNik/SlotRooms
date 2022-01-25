package com.testing.slotrooms.utils

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

fun atStartOfDay(date: Date): Date? {
    val localDateTime: LocalDateTime = dateToLocalDateTime(date)
    val startOfDay: LocalDateTime = localDateTime.with(LocalTime.MIN)
    return localDateTimeToDate(startOfDay)
}

fun atEndOfDay(date: Date): Date? {
    val localDateTime: LocalDateTime = dateToLocalDateTime(date)
    val endOfDay: LocalDateTime = localDateTime.with(LocalTime.MAX)
    return localDateTimeToDate(endOfDay)
}

fun dateToLocalDateTime(date: Date): LocalDateTime {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
}

fun localDateTimeToDate(localDateTime: LocalDateTime): Date? {
    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
}