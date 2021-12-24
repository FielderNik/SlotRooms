package com.testing.slotrooms.presentation.slots

import androidx.lifecycle.ViewModel
import com.testing.slotrooms.domain.slots.*
import kotlinx.coroutines.flow.asFlow
import java.util.*

class SlotsViewModel: ViewModel() {

    val slots = listOf<SlotsModel>(
        SlotsModel(
            roomName = "Room 1",
            startMeeting = Date(1640289600*1000L),
            finishMeeting = Date(1640275200 * 1000L),
            owner = userVasya,
            members = listOf(userMasha, userVova)
        ),
        SlotsModel("Room 1", Date(1640358000 * 1000L), Date(1640361600 * 1000L), userMasha, listOf(userPetya, userVova, userVasya)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userVasya, listOf(userMasha)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userPetya, listOf(userMasha, userVova)),
        SlotsModel("Room 1", Date(1640358000 * 1000L), Date(1640361600 * 1000L), userMasha, listOf(userPetya, userVova, userVasya)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userVasya, listOf(userMasha)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userPetya, listOf(userMasha, userVova)),
        SlotsModel("Room 1", Date(1640358000 * 1000L), Date(1640361600 * 1000L), userMasha, listOf(userPetya, userVova, userVasya)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userVasya, listOf(userMasha)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userPetya, listOf(userMasha, userVova)),
        SlotsModel("Room 1", Date(1640358000 * 1000L), Date(1640361600 * 1000L), userMasha, listOf(userPetya, userVova, userVasya)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userVasya, listOf(userMasha)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userPetya, listOf(userMasha, userVova)),
        SlotsModel("Room 1", Date(1640358000 * 1000L), Date(1640361600 * 1000L), userMasha, listOf(userPetya, userVova, userVasya)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userVasya, listOf(userMasha)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userPetya, listOf(userMasha, userVova)),
        SlotsModel("Room 1", Date(1640358000 * 1000L), Date(1640361600 * 1000L), userMasha, listOf(userPetya, userVova, userVasya)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userVasya, listOf(userMasha)),
        SlotsModel("Room 1", Date(1640271600 * 1000L), Date(1640275200 * 1000L), userPetya, listOf(userMasha, userVova)),
    )
}