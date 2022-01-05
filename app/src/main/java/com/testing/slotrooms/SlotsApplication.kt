package com.testing.slotrooms

import android.app.Application
import android.content.Context


class SlotsApplication: Application() {

//    lateinit var appComponent: AppComponent

    override fun onCreate() {
//        appComponent = DaggerAppComponent.create()

        super.onCreate()

        appContext = this.applicationContext
    }

    companion object {

        lateinit var appContext : Context

    }
}

//val Context.appComponent: AppComponent
//    get() = when (this) {
//        is MessagesApp -> appComponent
//        else -> this.applicationContext.appComponent
//    }
//}