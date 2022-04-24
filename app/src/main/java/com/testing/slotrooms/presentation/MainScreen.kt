package com.testing.slotrooms.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.model.rememberSlotFilter
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.presentation.views.FilterGroup
import com.testing.slotrooms.presentation.views.ext.coloredShadow
import com.testing.slotrooms.presentation.views.icons.FilterIcon
import com.testing.slotrooms.presentation.views.rememberAppScreenState
import com.testing.slotrooms.presentation.views.rememberAppTopBarState
import com.testing.slotrooms.ui.theme.*
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import me.onebone.toolbar.rememberCollapsingToolbarState

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val scaffoldState = rememberScaffoldState()
    val appScreenState = rememberAppScreenState()
    val appTopBarState = rememberAppTopBarState()
    val slotFilterState = rememberSlotFilter()

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

    CompositionLocalProvider(
        LocalScreenState provides appScreenState,
        LocalTopBarState provides appTopBarState,
        LocalSlotFilter provides slotFilterState

    ) {
        Scaffold(
            bottomBar = {
                if (appScreenState.isShowBottomBar) {
                    BottomNavigationBar(navController = navController)
                }
            },
            floatingActionButton = {
                if (appScreenState.isShowAddFab) {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Screens.AddNewSlotScreen.screenRoute)
                        },
                        backgroundColor = IconColor,
                        contentColor = Color.White
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add_24),
                            contentDescription = null
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
//        isFloatingActionButtonDocked = true,
            topBar = {
                if (appScreenState.isShowTopBar) {
                    TopAppBarSlot(
                        modifier = Modifier
                            .fillMaxWidth(),
                        appTopBarState = appTopBarState,
                        navController = navController,
                        localSlotFilter = slotFilterState
                    )
                }
            },
            scaffoldState = scaffoldState,
            backgroundColor = MainBackgroundGrey2
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                NavigationGraph(
                    navController = navController,
                    scaffoldState = scaffoldState,
                )
            }
        }
    }


}

@Composable
private fun TopAppBarSlot(
    modifier: Modifier = Modifier,
    appTopBarState: AppTopBarState,
    navController: NavController,
    localSlotFilter: SlotFilter?
) {
    Surface(elevation = 1.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            Column {
                Row(verticalAlignment = Alignment.Bottom) {
                    if (appTopBarState.isShowBack) {
                        Box(modifier = Modifier.padding(end = 16.dp), contentAlignment = Alignment.BottomStart) {
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
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )
                    if (appTopBarState.isShowFilter) {
                        FilterIcon(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(
                                    onClick = {
                                        appTopBarState.onFilterClicked?.invoke()
                                    }
                                )
                        )
                    }
                    if (appTopBarState.isShowFilterReset) {
                        Box(
                            modifier = Modifier
                                .background(color = Color.Unspecified)
                                .clickable {
                                    appTopBarState.onFilterResetClicked?.invoke()
                                }
                                .border(width = 1.dp, color = RedMain, shape = RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                text = "Reset filters",
                                color = RedMain,
                                fontSize = 12.sp
                            )
                        }
                    }

                }
                if (localSlotFilter != null && localSlotFilter.isNotEmpty() && appTopBarState.isShowFilterValues) {
                    FilterGroup()
                }

            }
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
            .coloredShadow(Color.Blue, offsetY = 2.dp, shadowRadius = 2.dp, alpha = 0.05f)
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
        color = MainBlueBackground
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