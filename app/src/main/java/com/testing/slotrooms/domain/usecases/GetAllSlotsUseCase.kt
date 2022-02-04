package com.testing.slotrooms.domain.usecases

import android.util.Log
import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.domain.mappers.SlotsRoomsUsersEntityToSlotRoomMapper
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.presentation.model.SlotRoom
import java.lang.Exception
import javax.inject.Inject

class GetAllSlotsUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) : UseCase<None, List<SlotRoom>> {
    override suspend fun run(params: None): Either<Exception, List<SlotRoom>> {
        try {
            val slots = databaseRepository.getAllSlotsRoomsUsersEntities().map(SlotsRoomsUsersEntityToSlotRoomMapper())
            return Either.Right(slots)
        } catch (ex: Exception) {
            return Either.Left(ex)
        }
    }
}