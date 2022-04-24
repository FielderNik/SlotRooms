package com.testing.slotrooms.domain.repositoties.queries

import com.testing.slotrooms.presentation.model.SlotFilter

class GetSlotsRoomsUsersByFilterSQLQuery : (SlotFilter) -> (String) {
    override fun invoke(filter: SlotFilter): String {
        return buildString {
            append("SELECT * FROM Slots WHERE".trimIndent())
            if (filter.room != null && filter.owner == null && filter.beginDate == null && filter.endDate == null) {
                append(" roomId = '${filter.room!!.id}'")
            }
            if (filter.owner != null && filter.room == null && filter.beginDate == null && filter.endDate == null) {
                append(" ownerId = '${filter.owner!!.id}'")
            }
            if (filter.owner != null && filter.room != null && filter.beginDate == null && filter.endDate == null) {
                append(" roomId = '${filter.room!!.id}' AND ownerId = '${filter.owner!!.id}'")
            }
            if (filter.beginDate != null && filter.endDate != null && filter.room == null && filter.owner == null) {
                append(" startTime BETWEEN ${filter.beginDate} AND ${filter.endDate} OR endTime BETWEEN ${filter.beginDate} AND ${filter.endDate}")
            }
            if (filter.room != null && filter.beginDate != null && filter.endDate != null && filter.owner == null) {
                append(" roomId = '${filter.room!!.id}' AND (startTime BETWEEN ${filter.beginDate} AND ${filter.endDate} OR endTime BETWEEN ${filter.beginDate} AND ${filter.endDate})")
            }
            if (filter.owner != null && filter.beginDate != null && filter.endDate != null && filter.room == null) {
                append(" ownerId = '${filter.owner!!.id}' AND (startTime BETWEEN ${filter.beginDate} AND ${filter.endDate} OR endTime BETWEEN ${filter.beginDate} AND ${filter.endDate})")
            }
            if (filter.owner != null && filter.room != null && filter.beginDate != null && filter.endDate != null ) {
                append(" ownerId = '${filter.owner!!.id}' AND roomId = '${filter.room!!.id}' AND (startTime BETWEEN ${filter.beginDate} AND ${filter.endDate} OR endTime BETWEEN ${filter.beginDate} AND ${filter.endDate})")
            }
        }
    }
}

