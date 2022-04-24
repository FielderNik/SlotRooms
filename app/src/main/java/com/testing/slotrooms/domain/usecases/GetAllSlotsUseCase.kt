package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.domain.mappers.SlotsRoomsUsersEntityToSlotRoomMapper
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.domain.repositoties.SlotsRepository
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.model.SlotRoom
import javax.inject.Inject

class GetAllSlotsUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val slotsRepository: SlotsRepository
) : UseCase<GetAllSlotsUseCase.Params, List<SlotRoom>> {

    data class Params(val filter: SlotFilter?)

    override suspend fun run(params: Params): Either<Exception, List<SlotRoom>> {
        return try {
            val filter = params.filter
            val room = filter?.room
            if (filter == null || filterIsEmpty(filter)) {
                slotsRepository.getAllSlotRooms()
            } else {
                val slots = databaseRepository.getSlotsRoomsUsers(filter)
                Either.Right(slots.map(SlotsRoomsUsersEntityToSlotRoomMapper()))
            }
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }

    private fun filterIsEmpty(filter: SlotFilter?): Boolean {
        return (filter?.beginDate == null && filter?.endDate == null && filter?.room == null && filter?.owner == null)
    }
}