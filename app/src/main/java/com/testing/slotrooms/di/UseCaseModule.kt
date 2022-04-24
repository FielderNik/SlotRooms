package com.testing.slotrooms.di

import com.testing.slotrooms.domain.mappers.SlotsRoomsUsersEntityToSlotRoomMapper
import com.testing.slotrooms.domain.repositoties.*
import com.testing.slotrooms.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object UseCaseModule {

    @Provides
    fun provideAddDefaultRoomsUseCase(databaseRepository: DatabaseRepository): AddDefaultRoomsUseCase {
        return AddDefaultRoomsUseCase(databaseRepository = databaseRepository)
    }

    @Provides
    fun provideAddDefaultUsersUseCase(databaseRepository: DatabaseRepository): AddDefaultUsersUseCase {
        return AddDefaultUsersUseCase(databaseRepository = databaseRepository)
    }

    @Provides
    fun provideGetAllRoomsUseCase(roomsRepository: RoomsRepository, databaseRepository: DatabaseRepository, remoteRepository: RemoteRepository): GetAllRoomsUseCase {
        return GetAllRoomsUseCase(
            roomsRepository = roomsRepository,
            databaseRepository = databaseRepository,
            remoteRepository = remoteRepository
        )
    }

    @Provides
    fun provideGetAllUsersUseCase(usersRepository: UsersRepository): GetAllUsersUseCase {
        return GetAllUsersUseCase(usersRepository = usersRepository)
    }

    @Provides
    fun provideSaveNewSlotUseCase(slotsRepository: SlotsRepository): CreateSlotUseCase {
        return CreateSlotUseCase(slotRepository = slotsRepository)
    }

    @Provides
    fun provideGetAllSlotsUseCase(databaseRepository: DatabaseRepository, slotsRepository: SlotsRepository): GetAllSlotsUseCase {
        return GetAllSlotsUseCase(databaseRepository = databaseRepository, slotsRepository = slotsRepository)
    }

    @Provides
    fun provideAddNewRoomUseCase(roomsRepository: RoomsRepository): CreateRoomUseCase {
        return CreateRoomUseCase(roomsRepository = roomsRepository)
    }

    @Provides
    fun provideAddNewUserUseCase(usersRepository: UsersRepository): CreateUserUseCase {
        return CreateUserUseCase(usersRepository = usersRepository)
    }

    @Provides
    fun provideGetSlotRoomById(
        databaseRepository: DatabaseRepository,
        slotsRoomsUsersEntityToSlotRoomMapper: SlotsRoomsUsersEntityToSlotRoomMapper
    ): GetSlotRoomByIdUseCase {
        return GetSlotRoomByIdUseCase(databaseRepository, slotsRoomsUsersEntityToSlotRoomMapper)
    }

    @Provides
    fun provideDeleteSlotUseCase(
        databaseRepository: DatabaseRepository
    ): DeleteSlotUseCase {
        return DeleteSlotUseCase(databaseRepository)
    }
}