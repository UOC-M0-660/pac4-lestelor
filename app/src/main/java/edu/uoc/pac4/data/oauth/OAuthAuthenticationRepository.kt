package edu.uoc.pac4.data.oauth

/**
 * Created by alex on 11/21/20.
 */
class OAuthAuthenticationRepository(
    private val dataSource: OAuthDataSource
) : AuthenticationRepository {

    override fun isUserAvailable(): Boolean {
        return dataSource.isUserAvailable()
    }

    override suspend fun login(authorizationCode: String): Boolean {
        return dataSource.login(authorizationCode)
    }

    override suspend fun logout() {
        dataSource.logout()
    }

    override suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? {
        return dataSource.getTokens(authorizationCode)
    }
}