package edu.uoc.pac4.data.oauth

import android.util.Log
import edu.uoc.pac4.data.SessionManager
import edu.uoc.pac4.data.network.Endpoints
import io.ktor.client.*
import io.ktor.client.request.*

class OAuthDataSource (
        private val sessionManager: SessionManager,
        private val httpClient: HttpClient,
) {
    private val TAG = "OAuthDataSource"

    fun isUserAvailable(): Boolean {
        return sessionManager.isUserAvailable()
    }

    suspend fun login(authorizationCode: String): Boolean {
        try {
            val response = httpClient
                    .post<OAuthTokensResponse>(Endpoints.tokenUrl) {
                        parameter("client_id", OAuthConstants.clientID)
                        parameter("client_secret", OAuthConstants.clientSecret)
                        parameter("code", authorizationCode)
                        parameter("grant_type", "authorization_code")
                        parameter("redirect_uri", OAuthConstants.redirectUri)
                    }

            Log.d(TAG, "Got Access token ${response.accessToken}")

            // Save access token and refresh token using the SessionManager class
            sessionManager.saveAccessToken(response.accessToken)
            response.refreshToken?.let {
                sessionManager.saveRefreshToken(it)
            }

            return true
        } catch (t: Throwable) {
            Log.w(TAG, "Error Getting Access token", t)
            return false
        }
    }


    fun logout() {
        sessionManager.clearAccessToken()
        sessionManager.clearRefreshToken()
    }


    suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? {

        try {
            val response = httpClient
            .post<OAuthTokensResponse>(Endpoints.tokenUrl) {
                parameter("client_id", OAuthConstants.clientID)
                parameter("client_secret", OAuthConstants.clientSecret)
                parameter("code", authorizationCode)
                parameter("grant_type", "authorization_code")
                parameter("redirect_uri", OAuthConstants.redirectUri)
            }

            // Remove in the final version, only to check if logout works properly
            Log.d(TAG,"Oauth tokens: ${response.accessToken} ${response.refreshToken}")

            // Save access token and refresh token using the SessionManager class
            sessionManager.saveAccessToken(response.accessToken)
            response.refreshToken?.let { sessionManager.saveRefreshToken(it) }

            return response

        } catch (t: Throwable) {
        Log.w(TAG, "Error Getting Access token", t)
        return null
        }
    }




}
