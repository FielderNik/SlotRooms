package com.testing.slotrooms.domain.slots

import java.util.*

data class SlotsModel(
    val roomName: String,
    val startMeeting: Date,
    val finishMeeting: Date,
    val owner: User,
    val members: List<User>,
)
