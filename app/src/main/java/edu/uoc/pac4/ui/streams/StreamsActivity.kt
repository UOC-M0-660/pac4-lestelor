package edu.uoc.pac4.ui.streams

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.uoc.pac4.R
import edu.uoc.pac4.ui.login.LoginActivity
import edu.uoc.pac4.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_streams.*
import org.koin.android.viewmodel.ext.android.viewModel

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    // ViewModel
    private val viewModel: StreamsViewModel by viewModel()

    // List
    private val adapter = StreamsAdapter()
    private val layoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()
        // Swipe to Refresh Listener
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getStreams(refresh = true)
        }
        // Init LiveData Observers
        initObservers()
        // Get Streams
        viewModel.getStreams(refresh = true)
    }

    private fun initRecyclerView() {
        // Set Layout Manager
        recyclerView.layoutManager = layoutManager
        // Set Adapter
        recyclerView.adapter = adapter
        // Set Pagination Listener
        recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                viewModel.getStreams(refresh = false)
            }

            override fun isLastPage(): Boolean {
                return !viewModel.areMoreStreamsAvailable()
            }

            override fun isLoading(): Boolean {
                return swipeRefreshLayout.isRefreshing
            }
        })
    }

    private fun initObservers() {
        // Loading
        viewModel.isLoading.observe(this) {
            swipeRefreshLayout.isRefreshing = it
        }
        // Streams
        viewModel.streams.observe(this) {
            adapter.submitList(it)
        }
        // Logged out
        viewModel.isLoggedOut.observe(this) {
            if (it) {
                // Close Activity
                finish()
                // Open Login
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    // region Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate Menu
        menuInflater.inflate(R.menu.menu_streams, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_user -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // endregion
}