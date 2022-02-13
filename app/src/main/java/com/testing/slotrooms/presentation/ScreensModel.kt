package com.testing.slotrooms.presentation

import com.testing.slotrooms.R


sealed class Screens(val title: String, val icon: Int, val screenRoute: String) {
    object AddNewSlotScreen : Screens(
        title = "AddNewSlot",
        icon = R.drawable.ic_add_24,
        screenRoute = "AddNewSlotScreen?slotRoomId={slotRoomId}",
    ) {
        fun passSlotRoomId(slotRoomId: String?) : String  = "AddNewSlotScreen?slotRoomId=$slotRoomId"
    }

    object Slots : Screens("Slots", R.drawable.ic_slots, "SlotsScreen")
    object Settings : Screens("Settings", R.drawable.ic_settings, "SettingsScreen")
    object Filter : Screens("SlotFilter", R.drawable.ic_baseline_filter_list_24, "SlotFilterScreen")
}