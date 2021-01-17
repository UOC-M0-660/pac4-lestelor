package edu.uoc.pac4.ui.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import edu.uoc.pac4.R
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.data.user.model.User
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        // Init Listeners
        initListeners()
        // Init LiveData Observers
        initObservers()
        // Get User Profile
        viewModel.getUser()
    }

    private fun initListeners() {
        // Update Description Listener
        updateDescriptionButton.setOnClickListener {
            // Hide Keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
            // Update User Description
            viewModel.updateUser(description = userDescriptionEditText.text?.toString() ?: "")
        }
        // Logout Button Listener
        logoutButton.setOnClickListener {
            // Logout
            viewModel.logout()
        }
    }

    private fun initObservers() {
        // Loading
        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) VISIBLE else GONE
        }
        // User
        viewModel.user.observe(this) {
            it?.let { setUserInfo(it) }
                ?: showError(getString(R.string.error_profile))
        }
        // Logged Out
        viewModel.isLoggedOut.observe(this) {
            if (it) {
                // Close Activity
                finishAffinity()
                // Open Login
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
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