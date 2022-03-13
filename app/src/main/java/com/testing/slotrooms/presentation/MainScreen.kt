package com.testing.slotrooms.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.bottomnavbar.BottomNavigationBar
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.ui.theme.GreenDark
import kotlinx.coroutines.CoroutineScope

@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val appTopBarState = remember {
        mutableStateOf(AppTopBarState())
    }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddNewSlotScreen.screenRoute)
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
            TopBarSlot(appTopBarState = appTopBarState.value, navController = navController, coroutineScope = coroutineScope)

        },
        scaffoldState = scaffoldState,


        ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationGraph(navController = navController, appTopBarState = appTopBarState, scaffoldState = scaffoldState)
        }

    }

}

@Composable
private fun TopBarSlot(appTopBarState: AppTopBarState, navController: NavController, coroutineScope: CoroutineScope) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    )
    {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.BottomStart) {

                if (appTopBarState.isShowBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                navController.navigateUp()
                            }
                            .padding(4.dp)
                    )
                }
            }

            Text(
                text = appTopBarState.title,
                style = MaterialTheme.typography.h1,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.BottomEnd) {

                if (appTopBarState.isShowFilter) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                appTopBarState.onFilterClicked?.invoke()
                            }
                            .padding(4.dp)
                    )
                }
            }

        }
    }
}