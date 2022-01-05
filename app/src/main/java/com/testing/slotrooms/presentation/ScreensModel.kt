package com.testing.slotrooms.presentation


sealed class Screens(val route: String) {
    object SlotsScreen : Screens("SlotsScreen")
    object AddNewSlotScreen : Screens("AddNewSlotScreen/{isNewSlot}")
}