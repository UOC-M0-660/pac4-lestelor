package edu.uoc.pac4.ui.login.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.pac4.R
import edu.uoc.pac4.data.network.Endpoints
import edu.uoc.pac4.data.oauth.util.OAuthConstants
import edu.uoc.pac4.ui.LaunchActivity
import kotlinx.android.synthetic.main.activity_oauth.*
import org.koin.android.viewmodel.ext.android.viewModel

class OAuthActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private val viewModel: OAuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        // Init Live Data Observers
        initObservers()
        // Start Authentication Flow
        launchOAuthAuthorization()
    }

    /// Start Observing LiveData from the ViewModel
    private fun initObservers() {
        // Observe Loading
        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        // Observe Logged in State
        viewModel.isLoggedIn.observe(this) {
            if (it) {
                // User is Logged In
                // Restart app to navigate to the logged in screens
                startActivity(Intent(this@OAuthActivity, LaunchActivity::class.java))
                finish()
            } else {
                // User is not Logged In
                // Show Error Message
                Toast.makeText(
                    this@OAuthActivity, getString(R.string.error_oauth), Toast.LENGTH_LONG
                ).show()
                // Restart Activity
                startActivity(Intent(this@OAuthActivity, OAuthActivity::class.java))
                finish()
            }
        }
    }

    fun buildOAuthUri(): Uri {
        return Uri.parse(Endpoints.authorizationUrl)
            .buildUpon()
            .appendQueryParameter("client_id", OAuthConstants.clientID)
            .appendQueryParameter("redirect_uri", OAuthConstants.redirectUri)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", OAuthConstants.scopes.joinToString(separator = " "))
            .appendQueryParameter("state", OAuthConstants.uniqueState)
            .build()
    }

    private fun launchOAuthAuthorization() {
        //  Create URI
        val uri = buildOAuthUri()

        // Set webView Redirect Listener
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?.let {
                    // Check if this url is our OAuth redirect, otherwise ignore it
                    if (request.url.toString().startsWith(OAuthConstants.redirectUri)) {
                        // To prevent CSRF attacks, check that we got the same state value we sent, otherwise ignore it
                        val responseState = request.url.getQueryParameter("state")
                        if (responseState == OAuthConstants.uniqueState) {
                            // This is our request, obtain the code!
                            request.url.getQueryParameter("code")?.let { code ->
                                // Got it!
                                Log.d(TAG, "Here is the authorization code! $code")                                // Hide WebView
                                webView.visibility = View.GONE
                                // Notify ViewModel
                                viewModel.onAuthorizationCode(code)
                            } ?: run {
                                // User cancelled the login flow
                                // Handle error
                                Toast.makeText(
                                    this@OAuthActivity,
                                    getString(R.string.error_oauth),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        // Load OAuth Uri
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(uri.toString())
    }
}