package edu.uoc.pac3

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import edu.uoc.pac4.data.SessionManager
import edu.uoc.pac4.data.network.Endpoints
import edu.uoc.pac4.data.network.Network
import edu.uoc.pac4.data.oauth.AuthenticationRepository
import edu.uoc.pac4.data.oauth.OAuthAuthenticationRepository
import edu.uoc.pac4.data.oauth.OAuthDataSource
import edu.uoc.pac4.data.oauth.OAuthTokensResponse
import edu.uoc.pac4.data.streams.StreamsDataSource
import edu.uoc.pac4.data.streams.StreamsRepository
import edu.uoc.pac4.data.streams.TwitchStreamsRepository
import edu.uoc.pac4.data.user.TwitchUserRepository
import edu.uoc.pac4.data.user.UserDataSource
import edu.uoc.pac4.data.user.UserRepository
import edu.uoc.pac4.ui.login.oauth.OAuthViewModel
import edu.uoc.pac4.ui.profile.ProfileViewModel
import edu.uoc.pac4.ui.streams.StreamsActivity
import edu.uoc.pac4.ui.streams.StreamsViewModel
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by alex on 24/10/2020.
 */

@RunWith(AndroidJUnit4::class)


@LargeTest
class Ex1Test: TwitchTest()  {

    private lateinit var streamsViewModel: StreamsViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var oauthViewModel: OAuthViewModel
    private lateinit var httpClient: HttpClient
    val refreshToken = "fzscymlbf7ox4mj9yg650ji47mlwd6d8t3b8y4iw30uxq3f2ti"
    val sharedPrefsWaitingMillis = 500L

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        fun httpClient(context: Context): HttpClient = Network.createHttpClient(context)
        fun streamsDataSource(): StreamsDataSource = StreamsDataSource(httpClient(context))
        fun repositoryStreams(): StreamsRepository = TwitchStreamsRepository(streamsDataSource())

        fun sessionManager(): SessionManager = SessionManager(context)
        fun dataSource(): OAuthDataSource = OAuthDataSource(sessionManager(), httpClient(context))
        fun repositoryOauth(): AuthenticationRepository = OAuthAuthenticationRepository(dataSource())

        fun userDataSource(): UserDataSource = UserDataSource(sessionManager(),httpClient(context))
        fun userRepository(): UserRepository = TwitchUserRepository(userDataSource())

        streamsViewModel = StreamsViewModel(repositoryStreams(), repositoryOauth())
        profileViewModel = ProfileViewModel(userRepository(), repositoryOauth())
        oauthViewModel = OAuthViewModel(repositoryOauth())
        httpClient = httpClient(context)
    }

    // check streamsVievModel accessing backend and returning valid streams
    @Test
    fun getStreamsValidAccessToken() {
        val context: Context = ApplicationProvider.getApplicationContext()
        runBlocking {
            setAccessToken(context, "efwo35z4mgyiyhje8bbp73b98oyavf")
        }
        val scenario = ActivityScenario.launch(StreamsActivity::class.java)
        scenario.onActivity {
            streamsViewModel.getStreams(null)
            runBlocking {
                streamsViewModel.getStreams(null)
                streamsViewModel.streams.observe(it, {
                    assert(!streamsViewModel.streams.value?.second.isNullOrEmpty())
                })
            }
        }
        scenario.close()
    }

    // Invalid client_id returns invalid token
    @Test
    fun getStreamsInvalidAccessToken() {
        val context: Context = ApplicationProvider.getApplicationContext()
        runBlocking {
            setAccessToken(context, "1234")
        }
        assert(SessionManager(context).getAccessToken().isNullOrEmpty())
    }

    // Check logout clears access token
    @Test
    fun logout() {
        val context: Context = ApplicationProvider.getApplicationContext()
        runBlocking {
            setAccessToken(context, "efwo35z4mgyiyhje8bbp73b98oyavf")
            profileViewModel.logout()
            delay(sharedPrefsWaitingMillis)
            assert(SessionManager(context).getAccessToken().isNullOrEmpty())
        }
    }

    // Token Refresh
    suspend fun setAccessToken(context: Context, client_id: String) {
        val response =
                httpClient.post<OAuthTokensResponse>(Endpoints.tokenUrl) {
                    parameter("client_id", client_id)
                    parameter("client_secret", "7fl44yqjm5tjdx73z45dd9ybwuuiez")
                    parameter("refresh_token", refreshToken)
                    parameter("grant_type", "refresh_token")
                }
        // Save new access token
        SessionManager(context).saveAccessToken(response.accessToken)
        delay(sharedPrefsWaitingMillis)
    }

}