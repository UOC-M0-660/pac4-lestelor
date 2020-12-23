package edu.uoc.pac4.data.streams

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import edu.uoc.pac4.R
import edu.uoc.pac4.data.SessionManager
import edu.uoc.pac4.data.TwitchApiService
import edu.uoc.pac4.data.network.Endpoints
import edu.uoc.pac4.data.network.Network
import edu.uoc.pac4.data.network.UnauthorizedException
import edu.uoc.pac4.ui.login.LoginActivity
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.launch

class StreamsDataSource (private val httpClient: HttpClient) {

    private val TAG = "StreamsDataSource"

    @Throws(UnauthorizedException::class)
    suspend fun getStreams(cursor: String? = null): Pair<String?, List<Stream>> {
        Log.d(TAG, "Requesting streams with cursor $cursor")


        try {
            val response = httpClient
                    .get<StreamsResponse>(Endpoints.streamsUrl) {
                        cursor?.let { parameter("after", it) }
                    }
            val streams = response.data.orEmpty()
            // Save cursor for next request
            val nextCursor = response.pagination?.cursor
            return Pair(nextCursor, streams)

        } catch (t: Throwable) {
            Log.w(TAG, "Error getting streams", t)
            // Try to handle error
            return when (t) {
                is ClientRequestException -> {
                    // Check if it's a 401 Unauthorized
                    if (t.response?.status?.value == 401) {
                        throw UnauthorizedException
                    }
                    Pair(null, listOf())
                }
                else -> Pair(null, listOf())
            }
        }
    }
}