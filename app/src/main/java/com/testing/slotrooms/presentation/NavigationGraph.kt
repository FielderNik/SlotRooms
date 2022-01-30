package com.testing.slotrooms.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.testing.slotrooms.presentation.addnewslot.AddNewSlotScreen
import com.testing.slotrooms.presentation.settings.SettingsScreen
import com.testing.slotrooms.presentation.slots.SlotsScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.Slots.screenRoute) {
        composable(Screens.Slots.screenRoute) {
            SlotsScreen(navController = navController)
        }
        composable(Screens.Settings.screenRoute) {
            SettingsScreen()
        }
        composable(
            route = Screens.AddNewSlotScreen.screenRoute,
            arguments = listOf(
                navArgument("isNewSlot") {
                    type = NavType.BoolType
                })
        ) {
            val isNewSlot = it.arguments?.getBoolean("isNewSlot", true) ?: true
            AddNewSlotScreen(isNewSlot = isNewSlot)
        }

    }
}