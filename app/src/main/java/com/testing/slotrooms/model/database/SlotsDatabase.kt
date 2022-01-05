package com.testing.slotrooms.model.database



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.testing.slotrooms.model.database.entities.Rooms
import com.testing.slotrooms.model.database.entities.Slots
import com.testing.slotrooms.model.database.entities.Users

@Database(entities = [Users::class, Rooms::class, Slots::class], version = 1)
abstract class SlotsDatabase : RoomDatabase() {
    abstract fun slotsDao(): SlotsDao


    companion object {
        @Volatile
        private var INSTANCE: SlotsDatabase? = null

        fun getDatabase(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) =
            Room
                .databaseBuilder(context, SlotsDatabase::class.java, "slots_db.db")
                .fallbackToDestructiveMigration()
                .build()

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                insertDefaultData(INSTANCE!!)
            }
        }

        private fun insertDefaultData(db: SlotsDatabase) {
            val slotDao = db.slotsDao()
            slotDao.insertUser(
                Users(name = "Alex")
            )
            slotDao.insertRoom(
                Rooms(name = "Room Office")
            )

            slotDao.insertSlot(
                Slots(
                    id = 1,
                    start = 2,
                    end = 3,
                    roomId = 1,
                    ownerId = 1,
                    comment = "Test comment for Room Office"
                )
            )
        }
    }


//    companion object {
//
//
//        private var instanceDb: SlotsDatabase? = null
//
//        @Synchronized
//        fun getInstance(context: Context): SlotsDatabase {
//            if(instanceDb == null) {
//                instanceDb = Room
//                    .databaseBuilder(
//                        context.applicationContext,
//                        SlotsDatabase::class.java,
//                        "slots_db")
//                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
//                    .build()
//            }
//            return instanceDb!!
//        }
//
//
//    }
}