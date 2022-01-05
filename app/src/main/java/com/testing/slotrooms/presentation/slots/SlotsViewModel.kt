package com.testing.slotrooms.presentation.slots

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.SlotsApplication
import com.testing.slotrooms.domain.slots.*
import com.testing.slotrooms.model.database.SlotsDatabase
import com.testing.slotrooms.model.database.entities.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SlotsViewModel() : ViewModel() {

    var db: SlotsDatabase = SlotsDatabase.getDatabase(SlotsApplication.appContext)

    fun addUser() {
        viewModelScope.launch (Dispatchers.IO) {
            val newUser = Users(id = 2, name = "Anna")
            db.slotsDao().insertUser(newUser)
        }
    }


    val slots = listOf<SlotsModel>(
        SlotsModel(
            roomName = "Room 1",
            startMeeting = Date(1640289600 * 1000L),
            finishMeeting = Date(1640275200 * 1000L),
            owner = userVasya,
            members = listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
        SlotsModel(
            "Room 1",
            Date(1640358000 * 1000L),
            Date(1640361600 * 1000L),
            userMasha,
            listOf(userPetya, userVova, userVasya)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userVasya,
            listOf(userMasha)
        ),
        SlotsModel(
            "Room 1",
            Date(1640271600 * 1000L),
            Date(1640275200 * 1000L),
            userPetya,
            listOf(userMasha, userVova)
        ),
    )
}