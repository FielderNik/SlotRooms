package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.core.flatMap
import com.testing.slotrooms.data.database.entities.SlotEntity
import com.testing.slotrooms.domain.repositoties.SlotsRepository
import javax.inject.Inject

class CreateSlotUseCase @Inject constructor(
    private val slotRepository: SlotsRepository
) : UseCase<SlotEntity, None> {
    override suspend fun run(slotEntity: SlotEntity): Either<Exception, None> {
        return try {
            return slotRepository.createSlot(slotEntity).flatMap {
                Either.Right(None)
            }
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}