package com.testing.slotrooms.presentation.addnewslot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testing.slotrooms.SlotsApplication
import com.testing.slotrooms.domain.slots.User
import com.testing.slotrooms.model.database.SlotsDatabase
import com.testing.slotrooms.model.database.entities.Rooms
import com.testing.slotrooms.model.database.entities.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

class ChoiceRoomViewModel: ViewModel() {

    private val _rooms: MutableStateFlow<List<Rooms>> = MutableStateFlow(listOf(Rooms(1, "Office")))
    val rooms: StateFlow<List<Rooms>> = _rooms

    private val _owners: MutableStateFlow<List<Users>> = MutableStateFlow(listOf(Users(1, "Ivanov Ivan")))
    val owners: StateFlow<List<Users>> = _owners

    val defaultRooms = listOf("Office", "Cabinet", "Java room", "Angular room", "Office", "Cabinet", "Java room")
    val defaultOwners = listOf("Ivan Popov", "Pavel Ivanov", "Alexandr Petrov", "Petr Alexandrov", "Anna Ishman", "Igor Lapshov", "Alexey Borovikov")
    var db: SlotsDatabase = SlotsDatabase.getDatabase(SlotsApplication.appContext)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val roomsDb = db.slotsDao().getAllRooms()
            if (roomsDb.size < 2) {
                addDefaultRooms()
            }
            val updatedRooms = db.slotsDao().getAllRooms()
            _rooms.emit(updatedRooms)

            val usersDb = db.slotsDao().getAllUsers()
            if (usersDb.size < 2) {
                addDefaultUsers()
            }
            val updatedUsers = db.slotsDao().getAllUsers()
            _owners.emit(updatedUsers)

        }



    }

    private suspend fun addDefaultUsers() {
        defaultOwners.forEachIndexed{ index, value ->
            val user = Users(id = index + 10, name = value)
            db.slotsDao().insertUser(user)
        }

    }

    private suspend fun addDefaultRooms() {
        defaultRooms.forEachIndexed{ index, value ->
            val room = Rooms(id = index + 10, name = value)
            db.slotsDao().insertRoom(room)
        }
    }
}