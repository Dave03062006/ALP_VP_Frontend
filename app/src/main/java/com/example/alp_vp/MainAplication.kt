package com.example.alp_vp

import android.app.Application
import com.example.alp_vp.data.container.AppContainer

class MainApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
