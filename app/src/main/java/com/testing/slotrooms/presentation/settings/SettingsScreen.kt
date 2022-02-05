package com.testing.slotrooms.presentation.settings

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.ui.theme.GreenMain
import com.testing.slotrooms.ui.theme.GreyIcon
import com.testing.slotrooms.ui.theme.MainBlue
import com.testing.slotrooms.ui.theme.YellowMain


@Composable
fun SettingsScreen(
    appTopBarState: MutableState<AppTopBarState>,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val resources = LocalContext.current.resources
    val settingsScreenState = viewModel.settingsScreenState.collectAsState()

    LaunchedEffect(Unit) {
        setupTopBar(appTopBarState = appTopBarState, resources = resources)
    }

    LaunchedEffect(settingsScreenState) {
        viewModel.handleEvent(SettingsScreenEvent.OpenDefaultScreen)
    }

    when (val state = settingsScreenState.value) {
        is SettingsScreenState.DefaultState -> {
            SettingsScreenContent(viewModel = viewModel)
        }
        is SettingsScreenState.NewUserDialogOpen -> {
            NewUserDialog(viewModel = viewModel)
        }
        is SettingsScreenState.NewRoomDialogOpen -> {
            NewRoomDialog(viewModel = viewModel)
        }

    }

}

@Composable
fun SettingsScreenContent(viewModel: SettingsViewModel) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        NewRoomItem(viewModel = viewModel)
        NewUserItem(viewModel = viewModel)
        EmptySlotItem()
    }
}

@Composable
private fun NewRoomItem(viewModel: SettingsViewModel) {
    Box(
        modifier = Modifier
            .height(108.dp)
            .fillMaxWidth()
            .background(MainBlue)
            .clickable {
                viewModel.handleEvent(SettingsScreenEvent.NewRoom.NewRoomClicked)
            }

    ) {
        Row(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.title_new_room),
                    style = MaterialTheme.typography.h2
                )
                Divider(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(40.dp)
                        .background(Color.Black), thickness = 2.dp
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 16.dp), contentAlignment = Alignment.BottomEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_new_room),
                    contentDescription = "New room", tint = GreyIcon
                )
            }

        }
    }
}

@Composable
private fun EmptySlotItem() {
    Box(
        modifier = Modifier
            .height(108.dp)
            .fillMaxWidth()
            .background(YellowMain)

    ) {
        Row(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.title_new_empty_slot),
                    style = MaterialTheme.typography.h2
                )
                Divider(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(40.dp)
                        .background(Color.Black), thickness = 2.dp
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 16.dp, bottom = 4.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_empty_slot),
                    contentDescription = null,
                    tint = GreyIcon
                )
            }

        }
    }
}

@Composable
private fun NewUserItem(viewModel: SettingsViewModel) {
    Box(
        modifier = Modifier
            .height(108.dp)
            .fillMaxWidth()
            .background(GreenMain)
            .clickable {
                viewModel.handleEvent(SettingsScreenEvent.NewUser.NewUserClicked)
            }

    ) {
        Row(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_new_user),
                    contentDescription = null,
                    tint = GreyIcon
                )
            }

            Column(
                modifier = Modifier
                    .padding(end = 16.dp, top = 16.dp),
                horizontalAlignment = Alignment.End

            ) {
                Text(
                    text = stringResource(id = R.string.title_new_user),
                    style = MaterialTheme.typography.h2
                )
                Divider(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(40.dp)
                        .background(Color.Black), thickness = 2.dp
                )
            }
        }
    }
}

private fun setupTopBar(appTopBarState: MutableState<AppTopBarState>, resources: Resources) {
    appTopBarState.value = appTopBarState.value.copy(
        title = resources.getString(R.string.title_settings),
        isShowBack = false,
        isShowFilter = false
    )
}