package com.testing.slotrooms.presentation.slots

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.Screens
import com.testing.slotrooms.presentation.model.SlotFilter
import com.testing.slotrooms.presentation.model.SlotRoom
import com.testing.slotrooms.presentation.views.AppScreenState
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.presentation.views.LoadingView
import com.testing.slotrooms.ui.theme.LocalScreenState
import com.testing.slotrooms.ui.theme.LocalSlotFilter
import com.testing.slotrooms.ui.theme.LocalTopBarState


@ExperimentalMaterialApi
@Composable
fun SlotsScreen(
    viewModel: SlotsViewModel = hiltViewModel(),
    navController: NavHostController,
    scaffoldState: ScaffoldState,
) {
    val slotsScreenState = viewModel.slotsScreenState.collectAsState()
    val resources = LocalContext.current.resources
    val appScreenState = LocalScreenState.current
    val topBarState = LocalTopBarState.current
    val localSlotFilter = LocalSlotFilter.current

    LaunchedEffect(Unit) {
        setupTopBar(
            appTopBarState = topBarState,
            resources = resources,
            navController = navController,
        )
        setupAppScreenState(appScreenState)
        localSlotFilter?.onFilterChanged = { viewModel.onFilterCleared(localSlotFilter) }
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(SlotsScreenEvent.SlotsEnterScreenEvent(localSlotFilter))
    }

    LaunchedEffect(Unit) {
        viewModel.effectChannel.collect {
            handleEffect(
                scaffoldState = scaffoldState,
                effect = it,
                resources = resources,
                navController = navController
            )
        }
    }

    SlotsScreenContent(
        slotsScreenState = slotsScreenState,
        navController = navController,
        viewModel = viewModel,
        filter = localSlotFilter
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SlotsScreenContent(
    slotsScreenState: State<SlotsScreenState>,
    navController: NavController,
    viewModel: SlotsViewModel,
    filter: SlotFilter?
) {
    when (val state = slotsScreenState.value) {
        is SlotsScreenState.SlotsEmptyScreen -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Empty")
            }
        }
        is SlotsScreenState.SlotsLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingView()
            }
        }
        is SlotsScreenState.SlotsSuccess -> {
            SlotsList(
                slots = state.slots,
                navController = navController,
                viewModel = viewModel,
                filter = filter,
            )
        }
        is SlotsScreenState.FilterOpened -> {
        }
    }
}

private fun setupTopBar(
    appTopBarState: AppTopBarState,
    resources: Resources,
    navController: NavHostController,
) {
    appTopBarState.apply {
        title = resources.getString(R.string.title_slots)
        isShowBack = false
        isShowFilter = true
        onFilterClicked = { navController.navigate(Screens.Filter.screenRoute) }
        isShowFilterValues = true
        isShowFilterReset = false
    }

}

private fun setupAppScreenState(appScreenState: AppScreenState) {
    appScreenState.provideFullScreen()
}

@ExperimentalMaterialApi
@Composable
private fun SlotsList(
    slots: List<SlotRoom>,
    navController: NavController,
    viewModel: SlotsViewModel,
    filter: SlotFilter?,
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
//        item {
//            Spacer(modifier = Modifier.height(8.dp))
//        }
        items(
            items = slots,
            key = { slot ->
                slot.id
            }
        ) { slot ->
//            SlotCardItem(viewModel = viewModel, slot = slot, navController = navController, filter = filter)
            SlotCard(viewModel = viewModel, slot = slot, navController = navController, filter = filter)
        }
    }


}

private suspend fun handleEffect(
    scaffoldState: ScaffoldState,
    effect: SlotsScreenEffect?,
    resources: Resources,
    navController: NavHostController
) {
    when (effect) {
        is SlotsScreenEffect.ErrorLoading -> {
            scaffoldState.snackbarHostState.showSnackbar(effect.exception.message ?: "Error")
        }

    }
}