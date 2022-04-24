package com.testing.slotrooms.domain.usecases

import com.testing.slotrooms.core.Either
import com.testing.slotrooms.core.UseCase
import com.testing.slotrooms.core.flatMap
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.domain.repositoties.RoomsRepository
import java.util.*
import javax.inject.Inject

class CreateRoomUseCase @Inject constructor(
    private val roomsRepository: RoomsRepository,
) : UseCase<CreateRoomUseCase.Params, Boolean> {

    data class Params(
        val roomName: String,
        val capacity: Int,
        val address: String,
        val info: String
    )

    override suspend fun run(params: Params): Either<Exception, Boolean> {
        return try {
            val newRoom = RoomEntity(
                id = UUID.randomUUID().toString(),
                name = params.roomName,
                image = "",
                capacity = params.capacity,
                address = params.address,
                info = params.info
            )
            roomsRepository.createRoom(newRoom).flatMap {
                Either.Right(true)
            }
        } catch (ex: Exception) {
            Either.Left(ex)
        }
    }
}