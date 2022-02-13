package com.testing.slotrooms.presentation

import androidx.lifecycle.ViewModel
import com.testing.slotrooms.core.EventHandler

open class BaseViewModel<Event>: ViewModel(), EventHandler<Event>  {
    override fun handleEvent(event: Event) {

    }
}