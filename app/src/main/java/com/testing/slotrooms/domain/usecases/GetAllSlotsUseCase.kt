package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.domain.mappers.SlotsRoomsUsersEntityToSlotRoomMapper
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.model.SlotRoom
import javax.inject.Inject

class GetAllSlotsUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) : UseCase<GetAllSlotsUseCase.Params, List<SlotRoom>> {

    data class Params(val filter: SlotFilter?)

    override suspend fun run(params: Params): Either<Exception, List<SlotRoom>> {
        return try {
            val slots = if (params.filter == null) {
                databaseRepository.getAllSlotsRoomsUsersEntities()
            } else {
                databaseRepository.getSlotsRoomsUsers(params.filter)
            }
            Either.Right(slots.map(SlotsRoomsUsersEntityToSlotRoomMapper()))
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}