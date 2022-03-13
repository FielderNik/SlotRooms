package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.None
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.presentation.model.SlotRoom
import com.testing.slotrooms.presentation.model.toSlot
import javax.inject.Inject

class DeleteSlotUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) : UseCase<SlotRoom, None> {
    override suspend fun run(params: SlotRoom): Either<Exception, None> {
        return try {
            databaseRepository.deleteSlot(params.toSlot())
            Either.Right(None)
        } catch (ex: Exception) {
            Either.Left(ex)
        }

    }
}