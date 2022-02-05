package com.testing.slotrooms.di

import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object UseCaseModule {

    @Provides
    fun provideAddDefaultRoomsUseCase(databaseRepository: DatabaseRepository) : AddDefaultRoomsUseCase {
        return AddDefaultRoomsUseCase(databaseRepository = databaseRepository)
    }

    @Provides
    fun provideAddDefaultUsersUseCase(databaseRepository: DatabaseRepository) : AddDefaultUsersUseCase {
        return AddDefaultUsersUseCase(databaseRepository = databaseRepository)
    }

    @Provides
    fun provideGetAllRoomsUseCase(databaseRepository: DatabaseRepository) : GetAllRoomsUseCase {
        return GetAllRoomsUseCase(databaseRepository = databaseRepository)
    }

    @Provides
    fun provideGetAllUsersUseCase(databaseRepository: DatabaseRepository) : GetAllUsersUseCase {
        return GetAllUsersUseCase(databaseRepository = databaseRepository)
    }

    @Provides
    fun provideSaveNewSlotUseCase(databaseRepository: DatabaseRepository) : SaveNewSlotUseCase {
        return SaveNewSlotUseCase(databaseRepository = databaseRepository)
    }

    @Provides
    fun provideGetAllSlotsUseCase(databaseRepository: DatabaseRepository) : GetAllSlotsUseCase {
        return GetAllSlotsUseCase(databaseRepository = databaseRepository)
    }

    @Provides
    fun provideAddNewRoomUseCase(databaseRepository: DatabaseRepository) : AddNewRoomUseCase {
        return AddNewRoomUseCase(databaseRepository = databaseRepository)
    }
}