package com.testing.slotrooms.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.testing.slotrooms.data.database.entities.Rooms
import com.testing.slotrooms.data.database.entities.Slots
import com.testing.slotrooms.data.database.entities.Users

@Database(entities = [Users::class, Rooms::class, Slots::class], version = 4, exportSchema = false)
abstract class SlotsDatabase : RoomDatabase() {
    abstract fun slotsDao(): SlotsDao
}