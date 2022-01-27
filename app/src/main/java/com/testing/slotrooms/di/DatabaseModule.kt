package com.testing.slotrooms.di

import android.content.Context
import androidx.room.Room
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.domain.repositoties.DatabaseRepositoryImpl
import com.testing.slotrooms.model.database.SlotsDao
import com.testing.slotrooms.model.database.SlotsDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideSlotsDao(slotsDatabase: SlotsDatabase): SlotsDao {
        return slotsDatabase.slotsDao()
    }


    @Provides
    @Singleton
    fun provideSlotsDatabase(@ApplicationContext appContext: Context): SlotsDatabase {
        return Room
            .databaseBuilder(
                appContext,
                SlotsDatabase::class.java,
                "slots_db.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }



}