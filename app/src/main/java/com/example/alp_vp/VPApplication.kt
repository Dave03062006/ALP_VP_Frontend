package com.example.alp_vp

import android.app.Application
import com.example.alp_vp.data.container.AppContainer
import com.example.alp_vp.data.container.DefaultAppContainer

class VPApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
