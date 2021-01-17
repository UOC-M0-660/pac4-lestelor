package edu.uoc.pac4

import android.app.Application
import edu.uoc.pac4.data.di.dataModule
import edu.uoc.pac4.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin

/**
 * Entry point for the Application.
 */

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init Koin Dependency Injection
        startKoin {
            // Set Logger
            androidLogger()
            // Set Android Context
            androidContext(this@MyApplication)
            // Load Koin modules
            loadKoinModules(listOf(dataModule, uiModule))
        }
    }

}