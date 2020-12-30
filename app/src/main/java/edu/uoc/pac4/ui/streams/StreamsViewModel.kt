package edu.uoc.pac4.ui.streams

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import edu.uoc.pac4.data.streams.Stream
import edu.uoc.pac4.data.streams.StreamsRepository
import kotlinx.coroutines.launch


// Select StreamsViewModel. Then, click Alt-Enter and select Create test
// See pg. 151 Advanced Android App Architecture Yung Cheng

class StreamsViewModel(
        private val repositoryStreams: StreamsRepository,
        private val repositoryOauth: AuthenticationRepository
) : ViewModel() {

    val streams = MutableLiveData<Pair<String?,List<Stream>>>()

    fun getStreams(cursor: String?) {
        viewModelScope.launch {
            streams.postValue(repositoryStreams.getStreams(cursor))
        }
    }

    fun logout() {
        viewModelScope.launch {
            repositoryOauth.logout()
        }
    }
}

