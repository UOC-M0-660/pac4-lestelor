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

    private val adapter = StreamsAdapter()
    private val layoutManager = LinearLayoutManager(this)

    // Connect the Activity with the ViewModel using Koin
    private val viewModel: StreamsViewModel by viewModel()

    // Move it to the StreamsDataSource
    //private val twitchApiService = TwitchApiService(Network.createHttpClient(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)
        // Init RecyclerView
        initRecyclerView()
        // Swipe to Refresh Listener
        swipeRefreshLayout.setOnRefreshListener {
            getStreams()
        }
        // Get Streams
        getStreams()
    }

    private fun initRecyclerView() {
        // Set Layout Manager
        recyclerView.layoutManager = layoutManager
        // Set Adapter
        recyclerView.adapter = adapter
        // Set Pagination Listener
        recyclerView.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                getStreams(nextCursor)
            }

            override fun isLastPage(): Boolean {
                return nextCursor == null
            }

            override fun isLoading(): Boolean {
                return swipeRefreshLayout.isRefreshing
            }
        })
    }

    private var nextCursor: String? = null
    private fun getStreams(cursor: String? = null) {
        // Show Loading
        swipeRefreshLayout.isRefreshing = true

        // Get Twitch Streams


            //val streams = viewModel.getStreams(cursor)
            // other way to do the same
            viewModel.getStreams(cursor)
            viewModel.streams.observe(this, {
                // Hide Loading
                swipeRefreshLayout.isRefreshing = false
                if (it.first != null) {
                    // Update UI with Streams
                    if (cursor != null) {
                        // We are adding more items to the list
                        adapter.submitList(adapter.currentList.plus(it.second))
                    } else {
                        // It's the first n items, no pagination yet
                        adapter.submitList(it.second)
                    }
                } else {
                    // Clear local tokens
                    // call it from the viewModel
                    viewModel.logout()
                    // User was logged out, close screen and open login
                    finish()
                    startActivity(Intent(this@StreamsActivity, LoginActivity::class.java))
                }
            })

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