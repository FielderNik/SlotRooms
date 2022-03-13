package com.testing.slotrooms.presentation.slots

import android.content.res.Resources
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.presentation.views.LoadingSlots


@ExperimentalMaterialApi
@Composable
fun SlotsScreen(
    viewModel: SlotsViewModel = hiltViewModel(),
    navController: NavHostController,
    appTopBarState: MutableState<AppTopBarState>,
    scaffoldState: ScaffoldState,
    filter: SlotFilter? = null,
) {
    val slotsScreenState = viewModel.slotsScreenState.collectAsState()
//    val slotsScreenEffect = viewModel.slotsScreenEffect.collectAsState()
    val resources = LocalContext.current.resources

    LaunchedEffect(Unit) {
        setupTopBar(appTopBarState = appTopBarState, resources = resources, navController = navController)
    }

    LaunchedEffect(slotsScreenState) {
        viewModel.handleEvent(SlotsScreenEvent.SlotsEnterScreenEvent(filter))
    }

    LaunchedEffect(Unit) {
        viewModel.effectChannel.collect {
            handleEffect(
                scaffoldState = scaffoldState,
                effect = it,
                resources = resources,
                navController = navController
            )
            viewModel.resetErrorStatus()
        }
    }

    Column {

        when (val state = slotsScreenState.value) {
            is SlotsScreenState.SlotsEmptyScreen -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Empty")
                }
            }
            is SlotsScreenState.SlotsLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingSlots()
                }
            }
            is SlotsScreenState.SlotsSuccess -> {
                SlotsList(slots = state.slots, navController = navController, viewModel = viewModel, filter = filter)
            }

            is SlotsScreenState.FilterOpened -> {
            }
        }
    }


}

private fun setupTopBar(appTopBarState: MutableState<AppTopBarState>, resources: Resources, navController: NavHostController) {
    appTopBarState.value = appTopBarState.value.copy(
        title = resources.getString(R.string.title_slots),
        isShowBack = false,
        isShowFilter = true,
        onFilterClicked = { navController.navigate(Screens.Filter.screenRoute) }
    )
}


@ExperimentalMaterialApi
@Composable
fun SlotsList(
    slots: List<SlotRoom>,
    navController: NavController,
    viewModel: SlotsViewModel,
    filter: SlotFilter?
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(slots) { slot ->
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

