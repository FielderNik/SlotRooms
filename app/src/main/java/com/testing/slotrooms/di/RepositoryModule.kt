package com.testing.slotrooms.di

import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.domain.repositoties.DatabaseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindDatabaseRepository(databaseRepositoryImpl: DatabaseRepositoryImpl) : DatabaseRepository
}