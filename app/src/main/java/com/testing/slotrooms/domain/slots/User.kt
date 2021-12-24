package com.testing.slotrooms.domain.slots

data class User(
    val id: Int,
    val name: String,
    val avatar: String,
)

val userVasya = User(1, "Vasya", "")
val userPetya = User(2, "Petya", "")
val userVova = User(3, "Vova", "")
val userMasha = User(4, "Masha", "")
