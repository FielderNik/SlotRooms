package com.testing.slotrooms.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.database.entities.SlotEntity
import com.testing.slotrooms.data.database.entities.UserEntity

@Database(entities = [UserEntity::class, RoomEntity::class, SlotEntity::class], version = 12, exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class SlotsDatabase : RoomDatabase() {
    abstract fun slotsDao(): SlotsDao
}