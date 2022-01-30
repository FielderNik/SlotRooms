package com.testing.slotrooms.di

import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.domain.usecases.AddDefaultRoomsUseCase
import com.testing.slotrooms.domain.usecases.AddDefaultUsersUseCase
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
}