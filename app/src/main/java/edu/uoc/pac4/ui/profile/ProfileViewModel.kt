package edu.uoc.pac4.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import edu.uoc.pac4.data.oauth.OAuthTokensResponse
import edu.uoc.pac4.data.user.User
import edu.uoc.pac4.data.user.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: UserRepository,

    ): ViewModel() {
        val user = MutableLiveData<User?>()

        fun getUser() {
            viewModelScope.launch {
                user.postValue(repository.getUser())
            }
        }

        fun updateUser(description: String) {
            viewModelScope.launch {
                user.postValue(repository.updateUser(description))
            }
        }

        fun logout(){
            viewModelScope.launch {
                repository.logout()
            }
        }
    }
