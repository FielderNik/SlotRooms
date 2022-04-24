package com.testing.slotrooms.presentation.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class AppTopBarState(
    initTitle: String = "Slots",
    initIsShowBack: Boolean = false,
    initIsShowFilter: Boolean = false,
    initOnFilterClicked: (() -> Unit)? = null,
    initShowFilterValues: Boolean = false,
    initIsShowFilterReset: Boolean = false,
    initOnFilterResetClicked: (() -> Unit)? = null
) {
    var title: String by mutableStateOf(initTitle)
    var isShowBack: Boolean by mutableStateOf(initIsShowBack)
    var isShowFilter: Boolean by mutableStateOf(initIsShowFilter)
    var onFilterClicked: (() -> Unit)? by mutableStateOf(initOnFilterClicked)
    var isShowFilterValues by mutableStateOf(initShowFilterValues)
    var isShowFilterReset by mutableStateOf(initIsShowFilterReset)
    var onFilterResetClicked by mutableStateOf(initOnFilterResetClicked)
}


@androidx.compose.runtime.Composable
fun rememberAppTopBarState(): AppTopBarState {
    return remember {
        AppTopBarState()
    }
}