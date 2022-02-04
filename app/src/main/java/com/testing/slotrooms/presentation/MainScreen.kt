package com.testing.slotrooms.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.bottomnavbar.BottomNavigationBar
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.ui.theme.GreenDark

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val appTopBarState = remember {
        mutableStateOf(AppTopBarState())
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
//            BottomAppBar(cutoutShape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50))) {
//
//            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddNewSlotScreen.passIsNewSlot(true))
                },
                backgroundColor = GreenDark,
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_24),
                    contentDescription = null
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        topBar = {
            Row() {
                Text(text = appTopBarState.value.title)
            }

        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            NavigationGraph(navController = navController, appTopBarState = appTopBarState)
        }
    }

}