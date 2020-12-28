package edu.uoc.pac4.ui.login.oauth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import edu.uoc.pac4.data.oauth.OAuthTokensResponse
import kotlinx.coroutines.launch

class OAuthViewModel(
        private val repository: AuthenticationRepository
): ViewModel() {
    val tokensResponse = MutableLiveData<OAuthTokensResponse?>()

    fun getTokens(authorizationCode: String) {
        viewModelScope.launch {
            tokensResponse.postValue(repository.getTokens(authorizationCode))
        }
    }
}