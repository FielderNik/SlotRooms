package com.testing.slotrooms.presentation

import com.testing.slotrooms.R


sealed class Screens(val title: String, val icon: Int, val screenRoute: String) {
    object AddNewSlotScreen : Screens(title = "AddNewSlot", icon = R.drawable.ic_add_24, screenRoute = "AddNewSlotScreen/{isNewSlot}", ) {
        fun passIsNewSlot(isNewSlot: Boolean) : String =
            "AddNewSlotScreen/$isNewSlot"
    }

    object Slots : Screens("Slots", R.drawable.ic_slots, "SlotsScreen")
    object Settings : Screens("Settings", R.drawable.ic_settings, "SettingsScreen")
}