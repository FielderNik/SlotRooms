package com.testing.slotrooms.data.services

import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.data.database.entities.SlotEntity
import com.testing.slotrooms.data.database.entities.UserEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SlotService {

    // Slots
    @POST("slots/create_slot")
    suspend fun createSlot(@Body slot: SlotEntity) : Response<SlotEntity>

    @GET("slots/get_all_slots")
    suspend fun getAllSlots(): Response<List<SlotEntity>>

    @GET("slots/get_slots")
    suspend fun getSlotsByRoom(@Query ("roomId") roomId: String) : Response<List<SlotEntity>>

    // Rooms
    @POST("room/create_room")
    suspend fun createRoom(@Body room: RoomEntity): Response<RoomEntity>

    @GET("room/get_all_rooms")
    suspend fun getAllRooms(): Response<List<RoomEntity>>


    //Users
    @GET("user/get_all_users")
    suspend fun getAllUsers(): Response<List<UserEntity>>

    @POST("user/create_user")
    suspend fun createUser(@Body user : UserEntity) : Response<UserEntity>
}