package com.testing.slotrooms.presentation.views

import androidx.compose.runtime.*


class AppScreenState(
    initShowAddFab: Boolean = true,
    initIsShowTopBar: Boolean = true,
    initIsShowBottomBar: Boolean = true
) {
    var isShowAddFab: Boolean by mutableStateOf(initShowAddFab)
    var isShowTopBar: Boolean by mutableStateOf(initIsShowTopBar)
    var isShowBottomBar: Boolean by mutableStateOf(initIsShowBottomBar)

    fun provideEmptyScreen() {
        isShowAddFab = false
        isShowBottomBar = false
        isShowTopBar = false
    }

    fun provideFullScreen() {
        isShowAddFab = true
        isShowBottomBar = true
        isShowTopBar = true
    }
}

@Composable
fun rememberAppScreenState(
    isShowAddFab: Boolean = true
) : AppScreenState {
    return remember {
        AppScreenState(isShowAddFab)
    }
}
