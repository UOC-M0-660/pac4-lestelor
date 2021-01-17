package edu.uoc.pac4.ui.di

import edu.uoc.pac4.ui.LaunchViewModel
import edu.uoc.pac4.ui.login.oauth.OAuthViewModel
import edu.uoc.pac4.ui.profile.ProfileViewModel
import edu.uoc.pac4.ui.streams.StreamsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by alex on 11/21/20.
 */

val uiModule = module {
    // Launch
    viewModel { LaunchViewModel(repository = get()) }
    // OAuth
    viewModel { OAuthViewModel(repository = get()) }
    // Streams
    viewModel { StreamsViewModel(repository = get()) }
    // Profile
    viewModel { ProfileViewModel(userRepository = get(), authenticationRepository = get()) }
}