package com.adrena.kmp.upload

import android.app.Application
import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())
    }
}