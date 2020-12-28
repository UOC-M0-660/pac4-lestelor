package edu.uoc.pac4.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import edu.uoc.pac4.R
import edu.uoc.pac4.data.network.Network
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.data.SessionManager
import edu.uoc.pac4.data.network.UnauthorizedException
import edu.uoc.pac4.data.oauth.OAuthTokensResponse
import edu.uoc.pac4.data.user.User
import edu.uoc.pac4.ui.LaunchActivity
import edu.uoc.pac4.ui.login.oauth.OAuthActivity
import edu.uoc.pac4.ui.login.oauth.OAuthViewModel
import kotlinx.android.synthetic.main.activity_oauth.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.progressBar
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"

/*    private val twitchApiService = TwitchApiService(Network.createHttpClient(this))*/
    // Connect the Activity with the ViewModel using Koin
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        // Get User Profile
        lifecycleScope.launch {
            getUserProfile()
        }
        // Update Description Button Listener
        updateDescriptionButton.setOnClickListener {
            // Hide Keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
            // Update User Description
            lifecycleScope.launch {
                updateUserDescription(userDescriptionEditText.text?.toString() ?: "")
            }
        }
        // Logout Button Listener
        logoutButton.setOnClickListener {
            // code for deleting access and refresh token
            viewModel.logout()

            // Logout
            logout()
        }
    }

    private  fun getUserProfile() {
        progressBar.visibility = VISIBLE
        viewModel.getUser()
        viewModel.user.observe(this, Observer<User?> {user:User? ->
            user?.let {
                // Update the UI with the user data
                setUserInfo(it)}?:
                    // Error :(
                    showError(getString(R.string.error_profile))


        })
        // Before rearranging
/*        progressBar.visibility = VISIBLE
        // Retrieve the Twitch User Profile using the API
        try {
            twitchApiService.getUser()?.let { user ->
                // Success :)
                // Update the UI with the user data
                setUserInfo(user)
            } ?: run {
                // Error :(
                showError(getString(R.string.error_profile))
            }
            // Hide Loading
            progressBar.visibility = GONE
        } catch (t: UnauthorizedException) {
            onUnauthorized()
        }*/
    }


    private fun updateUserDescription(description: String) {
        progressBar.visibility = VISIBLE

        viewModel.updateUser(description)
        viewModel.user.observe(this, Observer<User?> {user:User? ->
            // Hide Loading
            progressBar.visibility = GONE
            user?.let {
                // Update the UI with the user data
                setUserInfo(it)}?:
                    // Error :(
                    showError(getString(R.string.error_profile))


        })

/*        // Update the Twitch User Description using the API
        try {
            twitchApiService.updateUserDescription(description)?.let { user ->
                // Success :)
                // Update the UI with the user data
                setUserInfo(user)
            } ?: run {
                // Error :(
                showError(getString(R.string.error_profile))
            }
            // Hide Loading
            progressBar.visibility = GONE
        } catch (t: UnauthorizedException) {
            onUnauthorized()
        }*/
    }

    private fun setUserInfo(user: User) {
        // Set Texts
        userNameTextView.text = user.userName
        userDescriptionEditText.setText(user.description ?: "")
        // Avatar Image
        user.profileImageUrl?.let {
            Glide.with(this)
                .load(user.getSizedImage(it, 128, 128))
                .centerCrop()
                .transform(CircleCrop())
                .into(imageView)
        }
        // Views
        viewsText.text = getString(R.string.views_text, user.viewCount)
    }

    private fun logout() {
/*        // Clear local session data
        SessionManager(this).clearAccessToken()
        SessionManager(this).clearRefreshToken()*/
        // Close this and all parent activities
        finishAffinity()
        // Open Login
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun onUnauthorized() {
        // removed. called in the userdatasoruce
/*        // Clear local access token
        SessionManager(this).clearAccessToken()*/
        // User was logged out, close screen and all parent screens and open login
        finishAffinity()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    // Override Action Bar Home button to just finish the Activity
    // not to re-launch the parent Activity (StreamsActivity)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            false
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}