package edu.uoc.pac4

import android.app.Application
import edu.uoc.pac4.data.di.dataModule
import edu.uoc.pac4.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

// To access the content use the reference (application as MyApplication). Not the case
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Start the Koin framework. https://blog.mindorks.com/kotlin-koin-tutorial
        startKoin {
            // use Koin logger
            printLogger()
            // define context
            androidContext(applicationContext)
            // declare modules
            modules(listOf(uiModule, dataModule))
        }

    }
}