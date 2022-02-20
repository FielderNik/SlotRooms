package com.testing.slotrooms.presentation

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.testing.slotrooms.presentation.addnewslot.AddNewSlotScreen
import com.testing.slotrooms.presentation.filter.SlotFilterScreen
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.settings.SettingsScreen
import com.testing.slotrooms.presentation.slots.SlotsScreen
import com.testing.slotrooms.presentation.views.AppTopBarState

@Composable
fun NavigationGraph(navController: NavHostController, appTopBarState: MutableState<AppTopBarState>, scaffoldState: ScaffoldState) {
    NavHost(navController = navController, startDestination = Screens.Slots.screenRoute) {
        composable(
            route = Screens.Slots.screenRoute,
        ) {
            val filter = navController.previousBackStackEntry?.arguments?.getParcelable<SlotFilter>("slotFilter")
            SlotsScreen(
                navController = navController,
                appTopBarState = appTopBarState,
                scaffoldState = scaffoldState,
                filter = filter
            )
        }
        composable(Screens.Settings.screenRoute) {
            SettingsScreen(
                appTopBarState = appTopBarState,
                navController = navController,
                scaffoldState = scaffoldState
            )
        }
        composable(
            route = Screens.AddNewSlotScreen.screenRoute,
            arguments = listOf(
                navArgument("slotRoomId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }

            )
        ) {
            val slotRoomId = it.arguments?.getString("slotRoomId", null)
            AddNewSlotScreen(
                slotRoomId = slotRoomId,
                appTopBarState = appTopBarState,
                navController = navController,
                scaffoldState = scaffoldState
            )
        }
        composable(
            route = Screens.Filter.screenRoute
        ) {
            SlotFilterScreen(
                navController = navController,
                appTopBarState = appTopBarState,
                scaffoldState = scaffoldState
            )
        }

    }
}