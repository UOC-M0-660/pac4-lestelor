package edu.uoc.pac4.ui.login.oauth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import kotlinx.coroutines.launch

/**
 * Created by alex on 12/09/2020.
 */

class OAuthViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    val isLoggedIn = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()

    fun onAuthorizationCode(authorizationCode: String) {
        // Got Authorization Token
        // Launch login with the ViewModel Scope
        // It is attached to the Activity, so this scope will be cancelled
        // if the Activity is closed
        viewModelScope.launch {
            // Set Loading to true
            isLoading.postValue(true)
            // Launch login
            val success = repository.login(authorizationCode)
            // Check result
            if (success) {
                // Set Logged In Value
                isLoggedIn.postValue(true)
            } else {
                // Set Logged In Value
                isLoggedIn.postValue(false)
            }
            // Set Loading to false
            isLoading.postValue(false)
        }
    }

}