package com.testing.slotrooms.presentation.addnewslot

import androidx.lifecycle.ViewModel
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.database.entities.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class ChoiceRoomViewModel: ViewModel() {

    private val _roomEntity: MutableStateFlow<List<RoomEntity>> = MutableStateFlow(listOf(RoomEntity(UUID.randomUUID().toString(), "Office", image = "", capacity = 8)))
    val roomEntity: StateFlow<List<RoomEntity>> = _roomEntity

    private val _owners: MutableStateFlow<List<UserEntity>> = MutableStateFlow(listOf(UserEntity(UUID.randomUUID().toString(), "Ivanov Ivan")))
    val owners: StateFlow<List<UserEntity>> = _owners

    val defaultRooms = listOf("Office", "Cabinet", "Java room", "Angular room", "Office", "Cabinet", "Java room")
    val defaultOwners = listOf("Ivan Popov", "Pavel Ivanov", "Alexandr Petrov", "Petr Alexandrov", "Anna Ishman", "Igor Lapshov", "Alexey Borovikov")
//    var db: SlotsDatabase = SlotsDatabase.getDatabase(SlotsApplication.appContext)

/*    init {
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
    }*/
}