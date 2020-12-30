package edu.uoc.pac4.ui.streams

import androidx.lifecycle.MutableLiveData
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import edu.uoc.pac4.data.streams.Stream
import edu.uoc.pac4.data.streams.StreamsRepository
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class StreamsViewModelTest {

    private lateinit var streamsViewModel: StreamsViewModel

    @Mock
    lateinit var repositoryStreams: StreamsRepository
    lateinit var repositoryOauth: AuthenticationRepository
    lateinit var streams: MutableLiveData<Pair<String?,List<Stream>>>

    @Before
    fun setup() {
        streamsViewModel = StreamsViewModel(repositoryStreams, repositoryOauth)
        streamsViewModel.getStreams(null)
        streams = streamsViewModel.streams
    }

    @Test
     fun getStreams() {
        streamsViewModel.getStreams(null)
        assertEquals(streams, streamsViewModel.streams)
    }
}