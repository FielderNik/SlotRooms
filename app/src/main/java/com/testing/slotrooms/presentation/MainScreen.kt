package com.testing.slotrooms.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.bottomnavbar.BottomNavigationBar
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.presentation.views.ext.coloredShadow
import com.testing.slotrooms.ui.theme.FabBackground
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import me.onebone.toolbar.rememberCollapsingToolbarState

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val appTopBarState = remember {
        mutableStateOf(AppTopBarState())
    }
    val scaffoldState = rememberScaffoldState()

    val toolbarHeight = 60.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val collapsingToolbarScaffoldState = rememberCollapsingToolbarScaffoldState()
    val collapsingToolbarState = rememberCollapsingToolbarState()

//    CollapsingToolbarScaffold(
//        modifier = Modifier.fillMaxSize(),
//        state = collapsingToolbarScaffoldState,
//        scrollStrategy = ScrollStrategy.EnterAlways,
//        toolbar = {
//            TopBarSlot(
//                appTopBarState = appTopBarState.value,
//                navController = navController,
//                coroutineScope = coroutineScope
//            )
//        }
//    ) {
//        Column() {
//            NavigationGraph(
//                navController = navController,
//                appTopBarState = appTopBarState,
//                scaffoldState = scaffoldState,
//            )
//            BottomNavigationBar(navController = navController)
//        }
//    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddNewSlotScreen.screenRoute)
                },
                backgroundColor = FabBackground,
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
            TopBarSlot(
                modifier = Modifier
                    .height(toolbarHeight)
                    .fillMaxWidth(),
                appTopBarState = appTopBarState.value,
                navController = navController,
            )
        },

        scaffoldState = scaffoldState,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavigationGraph(
                navController = navController,
                appTopBarState = appTopBarState,
                scaffoldState = scaffoldState,
            )
        }
    }
}

@Composable
private fun TopBarSlot(
    modifier: Modifier = Modifier,
    appTopBarState: AppTopBarState,
    navController: NavController,
) {

    Surface(
        modifier = modifier
            .coloredShadow(Color.Blue, offsetY = 4.dp, shadowRadius = 4.dp, alpha = 0.1f)
            .drawWithContent {
                val paddingPx = with(density) { 20.dp.toPx() }
                clipRect(
                    left = -paddingPx,
                    top = 0f,
                    right = size.width + paddingPx,
                    bottom = size.height + paddingPx
                ) {
                    this@drawWithContent.drawContent()
                }
            },
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            if (appTopBarState.isShowBack) {
                Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.CenterStart) {
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
                modifier = Modifier.weight(1f),
                text = appTopBarState.title,
//                style = MaterialTheme.typography.h1,
                fontSize = 24.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            if (appTopBarState.isShowFilter) {
                Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
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