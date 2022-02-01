package com.testing.slotrooms.domain.usecases

import android.util.Log
import com.testing.slotrooms.core.Either
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.model.database.entities.Slots
import javax.inject.Inject

class SaveNewSlotUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    suspend fun run(slotEntity: Slots): Either<Exception, List<Slots>> {
        return try {
            val slots = databaseRepository.getSlotsByRoomIdAndTime(slot = slotEntity)
            Log.d("milk", "busySlots: $slots")
            if (slots.isEmpty()) {
                databaseRepository.insertSlot(slotEntity)
            } else {
                throw Exception("Slot is busy")
            }
            Either.Right(slots)
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}