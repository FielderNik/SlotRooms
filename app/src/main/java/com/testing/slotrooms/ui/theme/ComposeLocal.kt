package com.testing.slotrooms.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.views.AppScreenState
import com.testing.slotrooms.presentation.views.AppTopBarState

//val LocalNotificationManager = staticCompositionLocalOf<NotificationManager> {
//    error("No AppEventManager provided")
//}

val LocalScreenState = staticCompositionLocalOf<AppScreenState> {
    error("No AppScreenState provided")
}

val LocalTopBarState = staticCompositionLocalOf<AppTopBarState> {
    error("No AppTopBarState provided")
}

val LocalSlotFilter = staticCompositionLocalOf<SlotFilter?> {
    error("No LocalSlotFilter provided")
}

//val LocalSlotFilter = compositionLocalOf { error("No LocalSlotFilter provided") }