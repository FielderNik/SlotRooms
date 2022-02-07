package com.testing.slotrooms.model.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.testing.slotrooms.model.database.entities.Rooms
import com.testing.slotrooms.model.database.entities.Slots
import com.testing.slotrooms.model.database.entities.Users

@Database(entities = [Users::class, Rooms::class, Slots::class], version = 3)
abstract class SlotsDatabase : RoomDatabase() {
    abstract fun slotsDao(): SlotsDao
}