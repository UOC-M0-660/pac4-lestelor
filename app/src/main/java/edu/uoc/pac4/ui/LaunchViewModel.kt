package edu.uoc.pac4.ui

import androidx.lifecycle.ViewModel
import edu.uoc.pac4.data.oauth.AuthenticationRepository

/**
 * Created by alex on 11/21/20.
 */

// This is a simple ViewModel example,
// you can connect to it from the LaunchActivity and use it
// or just remove it
class LaunchViewModel(
    private val repository: AuthenticationRepository
) : ViewModel() {

/*    // Live Data
    val isUserAvailable = MutableLiveData<Boolean>()*/

    var isUserAvailable: Boolean? = null


    // Public function that can be called from the view (Activity)
    fun getUserAvailability() {
        // Get Availability from Repository and post result
        // removed the viewmodelscope.launch since sharedpreferences is not an async service
/*        viewModelScope.launch {
            isUserAvailable.postValue(repository.isUserAvailable())
        }*/

        isUserAvailable = repository.isUserAvailable()
    }
}