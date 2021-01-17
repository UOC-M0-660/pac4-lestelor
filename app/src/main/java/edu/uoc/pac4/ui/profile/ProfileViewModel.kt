package edu.uoc.pac4.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import edu.uoc.pac4.data.network.UnauthorizedException
import edu.uoc.pac4.data.user.model.User
import edu.uoc.pac4.data.user.UserRepository
import kotlinx.coroutines.launch

/**
 * Created by alex on 13/09/2020.
 */

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {

    // Live Data
    val user = MutableLiveData<User?>()
    val isLoading = MutableLiveData<Boolean>()
    val isLoggedOut = MutableLiveData<Boolean>()

    /// Gets current logged in user
    fun getUser() {
        viewModelScope.launch {
            // Set Loading
            isLoading.postValue(true)
            // Get User
            try {
                val userResult = userRepository.getUser()
                // Post User Value
                user.postValue(userResult)
            } catch (e: UnauthorizedException) {
                isLoggedOut.postValue(true)
            }
            // Set Loading
            isLoading.postValue(false)
        }
    }

    /// Updates current logged in user
    fun updateUser(description: String) {
        viewModelScope.launch {
            // Set Loading
            isLoading.postValue(true)
            // Get User
            try {
                val userResult = userRepository.updateUser(description)
                // Post User Value
                user.postValue(userResult)
            } catch (e: UnauthorizedException) {
                isLoggedOut.postValue(true)
            }
            // Set Loading
            isLoading.postValue(false)
        }
    }

    /// Logs out current logged in user
    fun logout() {
        viewModelScope.launch {
            // Set Loading
            isLoading.postValue(true)
            // Logout
            authenticationRepository.logout()
            // Set Logged Out value
            isLoggedOut.postValue(true)
            // Set Loading
            isLoading.postValue(false)
        }
    }
}