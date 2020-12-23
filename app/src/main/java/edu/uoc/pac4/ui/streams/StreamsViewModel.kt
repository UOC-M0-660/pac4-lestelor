package edu.uoc.pac4.ui.streams

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uoc.pac4.data.streams.Stream
import edu.uoc.pac4.data.streams.StreamsRepository
import kotlinx.coroutines.launch

class StreamsViewModel(
        private val repository: StreamsRepository
) : ViewModel() {

    suspend fun getStreams(cursor: String?): Pair<String?, List<Stream>> {
        return repository.getStreams(cursor)
    }
}