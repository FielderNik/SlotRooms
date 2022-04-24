package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.domain.mappers.SlotsRoomsUsersEntityToSlotRoomMapper
import com.testing.slotrooms.domain.repositoties.DatabaseRepository
import com.testing.slotrooms.presentation.model.SlotRoom
import javax.inject.Inject

class GetSlotRoomByIdUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val slotsRoomsUsersEntityToSlotRoomMapper: SlotsRoomsUsersEntityToSlotRoomMapper
) : UseCase<String, SlotRoom> {

    override suspend fun run(params: String): Either<Exception, SlotRoom> {
        return try {
            val slotRoom =
                slotsRoomsUsersEntityToSlotRoomMapper.invoke(databaseRepository.getSlotRoomById(params)) //TODO (если в бд не нашли слота по этому id - обработать отдельно)
            Either.Right(slotRoom)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Either.Left(ex)
        }
    }
}