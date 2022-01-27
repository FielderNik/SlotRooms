package com.testing.slotrooms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.testing.slotrooms.presentation.Screens
import com.testing.slotrooms.presentation.addnewslot.AddNewSlotScreen
import com.testing.slotrooms.presentation.slots.SlotsScreen
import com.testing.slotrooms.presentation.slots.SlotsViewModel
import com.testing.slotrooms.ui.theme.MainBackground
import com.testing.slotrooms.ui.theme.SlotRoomsTheme
import dagger.hilt.android.AndroidEntryPoint

//class MainActivity : ComponentActivity() {
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            SlotRoomsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MainBackground) {
                    val slotsViewModel = viewModel<SlotsViewModel>()
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screens.SlotsScreen.route)
                    {
                        composable(Screens.SlotsScreen.route) {
                            SlotsScreen(viewModel = slotsViewModel, navController)
                        }
                        composable(
                            route = Screens.AddNewSlotScreen.route,
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
            }
        }
    }
}

/*
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SlotRoomsTheme {
//        Greeting(name = "WORLD!")
        val slotsViewModel = viewModel<SlotsViewModel>()
        SlotsScreen(viewModel = slotsViewModel)
    }
}*/
