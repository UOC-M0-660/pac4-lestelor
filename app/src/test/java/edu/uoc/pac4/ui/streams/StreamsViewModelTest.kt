package edu.uoc.pac4.ui.streams

import androidx.lifecycle.LifecycleOwner
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import edu.uoc.pac4.data.streams.StreamsRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class StreamsViewModelTest {

    private lateinit var streamsViewModel: StreamsViewModel


    @Mock
    lateinit var repositoryStreams: StreamsRepository
    @Mock
    lateinit var repositoryOauth: AuthenticationRepository
    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner


    @Before
    fun setup() {
        streamsViewModel = StreamsViewModel(repositoryStreams, repositoryOauth)
        streamsViewModel.getStreams(null)
    }

    @Test

     fun getStreams() {
        streamsViewModel.streams.observe(lifecycleOwner, {
            assertEquals(it, streamsViewModel.streams)
        })
    }
}