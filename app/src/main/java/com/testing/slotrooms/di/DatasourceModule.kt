package com.testing.slotrooms.di

import com.testing.slotrooms.data.services.Api
import com.testing.slotrooms.data.services.SlotService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object DatasourceModule {

    @Provides
    fun provideSlotsService(api: Api) : SlotService {
        return api.slotService
    }

}