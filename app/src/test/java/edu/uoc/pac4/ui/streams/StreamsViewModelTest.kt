package edu.uoc.pac4.ui.streams

import androidx.lifecycle.LifecycleOwner
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import edu.uoc.pac4.data.streams.StreamsRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.uoc.pac4.data.streams.Stream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import org.koin.android.scope.lifecycleScope
import java.util.concurrent.CompletableFuture

@RunWith(MockitoJUnitRunner::class)
class StreamsViewModelTest {

    private lateinit var streamsViewModel: StreamsViewModel


    @Mock
    lateinit var repositoryStreams: StreamsRepository
    @Mock
    lateinit var repositoryOauth: AuthenticationRepository
    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner
    @Mock
    private lateinit var streams: List<Stream>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        streamsViewModel = StreamsViewModel(repositoryStreams, repositoryOauth)
    }



    @Test
    fun getStreams() {

        //streams = repositoryStreams.getStreams(null).second

        streamsViewModel.getStreams(null)
        streamsViewModel.streams.postValue(Pair(null, streams))

        assertEquals(streams, streamsViewModel.streams)
    }
}