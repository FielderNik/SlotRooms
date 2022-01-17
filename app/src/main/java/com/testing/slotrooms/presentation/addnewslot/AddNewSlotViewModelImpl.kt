package com.testing.slotrooms.presentation.addnewslot

import androidx.lifecycle.ViewModel

abstract class AddNewSlotViewModel : ViewModel() {
    abstract val rooms: List<String>
}

class AddNewSlotViewModelImpl : AddNewSlotViewModel() {
    override val rooms = listOf("Office", "Cabinet", "Java room", "Angular room", "Office", "Cabinet", "Java room")
}

class AddNewSlotViewModelImplPreview(override val rooms: List<String>) : AddNewSlotViewModel()