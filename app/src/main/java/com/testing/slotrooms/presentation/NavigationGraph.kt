package com.testing.slotrooms.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.testing.slotrooms.presentation.addnewslot.AddNewSlotScreen
import com.testing.slotrooms.presentation.createroom.CreateRoomScreen
import com.testing.slotrooms.presentation.filter.SlotFilterScreen
import com.testing.slotrooms.presentation.settings.SettingsScreen
import com.testing.slotrooms.presentation.slots.SlotsScreen
import com.testing.slotrooms.presentation.splash.SplashScreen

@ExperimentalMaterialApi
@Composable
fun NavigationGraph(
    navController: NavHostController,
    scaffoldState: ScaffoldState
) {
    NavHost(navController = navController, startDestination = Screens.Splash.screenRoute) {
        composable(
            route = Screens.Slots.screenRoute,
        ) {
//            val filter = navController.previousBackStackEntry?.arguments?.getParcelable<SlotFilter>("slotFilter")
            SlotsScreen(
                navController = navController,
                scaffoldState = scaffoldState,
            )
        }
        composable(Screens.Settings.screenRoute) {
            SettingsScreen(
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
                navController = navController,
                scaffoldState = scaffoldState
            )
        }
        composable(
            route = Screens.Filter.screenRoute
        ) {
            SlotFilterScreen(
                navController = navController,
                scaffoldState = scaffoldState
            )
        }
        composable(
            route = Screens.Splash.screenRoute
        ) {
            SplashScreen(
                navController = navController,
                scaffoldState = scaffoldState
            )
        }

        composable(
            route = Screens.CreateRoom.screenRoute,
            arguments = listOf(
                navArgument("roomId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            val roomId = it.arguments?.getString("roomId", null)
            CreateRoomScreen(
                navController = navController,
                scaffoldState = scaffoldState,
                roomId = roomId
            )
        }

    }
}