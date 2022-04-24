package com.testing.slotrooms.presentation.createroom

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.testing.slotrooms.R
import com.testing.slotrooms.data.database.entities.RoomEntity
import com.testing.slotrooms.presentation.views.AppScreenState
import com.testing.slotrooms.presentation.views.AppTopBarState
import com.testing.slotrooms.presentation.views.LoadingView
import com.testing.slotrooms.presentation.views.buttons.ButtonBlock
import com.testing.slotrooms.ui.theme.*

@Composable
fun CreateRoomScreen(
    viewModel: CreateRoomViewModel = hiltViewModel(),
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    roomId: String?
) {
    val state = viewModel.state.collectAsState()
    val appScreenState = LocalScreenState.current
    val topBarState = LocalTopBarState.current
    val resources = LocalContext.current.resources

    LaunchedEffect(Unit) {
        setupTopBarState(topBarState, resources, roomId)
        setupAppScreenState(appScreenState)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            handleEffect(
                effect = effect,
                navController = navController,
                scaffoldState = scaffoldState,
                resources = resources
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(CreateRoomEvent.EntryScreen(roomId))
    }


    CreateRoomScreenContent(state.value, viewModel, navController)

}

private suspend fun handleEffect(
    effect: CreateRoomEffect?,
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    resources: Resources
) {
    when (effect) {
        is CreateRoomEffect.SaveError -> {
            scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.error_room_with_this_name_already_exists))
        }
        CreateRoomEffect.SaveSuccess -> {
            scaffoldState.snackbarHostState.showSnackbar(resources.getString(R.string.notice_room_saved_success))
            navController.navigateUp()
        }
        null -> {

        }
    }
}

private fun setupTopBarState(topBarState: AppTopBarState, resources: Resources, roomId: String?) {
    val title = if (roomId == null) resources.getString(R.string.title_new_room) else resources.getString(R.string.title_edit_room)
    topBarState.apply {
        this.title = title
        isShowBack = true

    }
}

private fun setupAppScreenState(appScreenState: AppScreenState) {
    appScreenState.apply {
        isShowAddFab = false
        isShowBottomBar = true
        isShowTopBar = true
    }
}

@Composable
private fun CreateRoomScreenContent(
    state: CreateRoomState,
    viewModel: CreateRoomViewModel,
    navController: NavHostController,
) {
    when (state) {
        CreateRoomState.EmptyRoom -> {
            Text(text = "Hello")
        }
        is CreateRoomState.RoomDisplayed -> {
            RoomScreen(
                room = state.room,
                viewModel = viewModel,
                navController = navController
            )
        }
        CreateRoomState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingView()
            }
        }
    }
}


@Composable
private fun RoomScreen(
    room: RoomEntity?,
    viewModel: CreateRoomViewModel,
    navController: NavHostController,
) {
    val scrollableState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(state = scrollableState)
    ) {
        val roomName = remember { mutableStateOf(room?.name ?: "") }
        val roomCapacity = remember { mutableStateOf(room?.capacity) }
        val roomAddress = remember { mutableStateOf(room?.address ?: "") }
        val roomInfo = remember { mutableStateOf(room?.info ?: "") }

        RoomNameBlock(roomName = roomName)
        Spacer(modifier = Modifier.height(16.dp))
        RoomCapacityBlock(roomCapacity = roomCapacity)
        Spacer(modifier = Modifier.height(16.dp))
        RoomAddressBlock(roomAddress = roomAddress)
        Spacer(modifier = Modifier.height(16.dp))
        RoomInfoBlock(roomInfo = roomInfo)

        Spacer(modifier = Modifier.weight(1f))
        ButtonBlock(
            modifier = Modifier.padding(top = 16.dp),
            onSaveClicked = {
                viewModel.saveRoom(
                    roomName = roomName.value,
                    capacity = roomCapacity.value ?: 0,
                    address = roomAddress.value,
                    info = roomInfo.value
                )
            },
            onCancelClicked = {
                navController.navigateUp()
            }
        )
    }
}

@Composable
private fun RoomNameBlock(roomName: MutableState<String>) {
    Text(
        modifier = Modifier.padding(start = 8.dp),
        text = stringResource(id = R.string.title_room_name),
        color = GreyFont.copy(alpha = 0.8f)
    )
    Spacer(modifier = Modifier.height(4.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = roomName.value,
        onValueChange = {
            roomName.value = it
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.placeholder_room_name),
                fontSize = 18.sp,
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = appOutlinedTextFieldColors(),
        textStyle = MaterialTheme.typography.h2,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_office_24),
                tint = MainBlue,
                contentDescription = ""
            )
        }
    )
}

@Composable
private fun RoomCapacityBlock(roomCapacity: MutableState<Int?>) {
    val text = if (roomCapacity.value == null) "" else roomCapacity.value.toString()
    Text(
        modifier = Modifier.padding(start = 8.dp),
        text = stringResource(id = R.string.title_room_capacity),
        color = GreyFont.copy(alpha = 0.8f)
    )
    Spacer(modifier = Modifier.height(4.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = {
            val capacity = it.toIntOrNull() ?: 0
            roomCapacity.value = capacity
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.placeholder_room_capacity),
                fontSize = 18.sp,
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = appOutlinedTextFieldColors(),
        textStyle = MaterialTheme.typography.h2,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_capacity),
                tint = MainBlue,
                contentDescription = ""
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun RoomAddressBlock(roomAddress: MutableState<String>) {
    Text(
        modifier = Modifier.padding(start = 8.dp),
        text = stringResource(id = R.string.title_room_address),
        color = GreyFont.copy(alpha = 0.8f)
    )
    Spacer(modifier = Modifier.height(4.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = roomAddress.value,
        onValueChange = {
            roomAddress.value = it
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.placeholder_room_address),
                fontSize = 18.sp,
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = appOutlinedTextFieldColors(),
        textStyle = MaterialTheme.typography.h2,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_address),
                tint = MainBlue,
                contentDescription = ""
            )
        },
    )
}


@Composable
private fun RoomInfoBlock(roomInfo: MutableState<String>) {
    Text(
        modifier = Modifier.padding(start = 8.dp),
        text = stringResource(id = R.string.title_room_info),
        color = GreyFont.copy(alpha = 0.8f)
    )
    Spacer(modifier = Modifier.height(4.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = roomInfo.value,
        onValueChange = {
            roomInfo.value = it
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.placeholder_room_info),
                fontSize = 18.sp,
            )
        },
        shape = RoundedCornerShape(8.dp),
        colors = appOutlinedTextFieldColors(),
        textStyle = MaterialTheme.typography.h2,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_info),
                tint = MainBlue,
                contentDescription = ""
            )
        },
    )
}