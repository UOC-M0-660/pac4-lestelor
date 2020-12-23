package edu.uoc.pac4.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer


import androidx.lifecycle.ViewModelProvider
import edu.uoc.pac4.R
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.data.SessionManager
import edu.uoc.pac4.ui.streams.StreamsActivity

import org.koin.android.viewmodel.ext.android.viewModel


class LaunchActivity : AppCompatActivity() {

    // Connect the Activity with the ViewModel using Koin
    private val viewModel: LaunchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        // Connect the Activity with the ViewModel observing any change in livedata isUserAvailable
        // It has to be defined the viewmodel in the uiModule
        viewModel.getUserAvailability()
        viewModel.isUserAvailable.observe(this, Observer {
            checkUserSession(it)
        })
    }

    private fun checkUserSession(isUserAvailable:Boolean) {
/*        if (SessionManager(this).isUserAvailable()) {*/
        if (isUserAvailable) {
            // User is available, open Streams Activity
            startActivity(Intent(this, StreamsActivity::class.java))
        } else {
            // User not available, request Login
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}