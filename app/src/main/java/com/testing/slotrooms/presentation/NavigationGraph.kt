package com.testing.slotrooms.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.testing.slotrooms.presentation.addnewslot.AddNewSlotScreen
import com.testing.slotrooms.presentation.settings.SettingsScreen
import com.testing.slotrooms.presentation.slots.SlotsScreen
import com.testing.slotrooms.presentation.views.AppTopBarState

@Composable
fun NavigationGraph(navController: NavHostController, appTopBarState: MutableState<AppTopBarState>) {
    NavHost(navController = navController, startDestination = Screens.Slots.screenRoute) {
        composable(Screens.Slots.screenRoute) {
            SlotsScreen(navController = navController, appTopBarState = appTopBarState)
        }
        composable(Screens.Settings.screenRoute) {
            SettingsScreen(appTopBarState = appTopBarState)
        }
        composable(
            route = Screens.AddNewSlotScreen.screenRoute,
            arguments = listOf(
                navArgument("isNewSlot") {
                    type = NavType.BoolType
                })
        ) {
            val isNewSlot = it.arguments?.getBoolean("isNewSlot", true) ?: true
            AddNewSlotScreen(isNewSlot = isNewSlot, appTopBarState = appTopBarState, navController = navController)
        }

    }
}