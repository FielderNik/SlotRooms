package com.testing.slotrooms.di

import com.testing.slotrooms.domain.repositoties.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindDatabaseRepository(databaseRepositoryImpl: DatabaseRepositoryImpl) : DatabaseRepository

    @Binds
    abstract fun bindRemoteRepository(remoteRepositoryImpl: RemoteRepositoryImpl) : RemoteRepository

    @Binds
    abstract fun bindUsersRepository(usersRepositoryImpl: UsersRepositoryImpl) : UsersRepository

    @Binds
    abstract fun bindRoomsRepository(roomsRepositoryImpl: RoomsRepositoryImpl) : RoomsRepository

    @Binds
    abstract fun bindSlotsRepository(slotsRepositoryImpl: SlotsRepositoryImpl) : SlotsRepository
}