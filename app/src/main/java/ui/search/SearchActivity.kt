package ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import domain.player.Track
import ui.player.PlayerActivity
import ui.search.SearchState.Loading
import ui.search.SearchState.SearchHistory
import ui.search.SearchState.SearchedTracks
import utils.isVisible

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_VALUE_KEY = "SEARCH_VALUE_KEY"
    }

    private var searchInputQuery = ""

    private lateinit var viewModel: SearchViewModel

    private val tracksAdapter by lazy {
        TracksAdapter { track ->
            OnClickListener {
                viewModel.addTrackToHistory(track)
                startPlayerActivity(track)
            }
        }
    }

    private val historyAdapter by lazy {
        TracksAdapter { track -> OnClickListener { startPlayerActivity(track) } }
    }

    private val backButton by lazy { findViewById<ImageView>(R.id.backButton) }
    private val searchEditText by lazy { findViewById<EditText>(R.id.searchEditText) }
    private val clearSearchButton by lazy { findViewById<ImageView>(R.id.clearSearchButton) }
    private val updateButton by lazy { findViewById<Button>(R.id.updateButton) }
    private val clearHistoryButton by lazy { findViewById<Button>(R.id.clearHistoryButton) }
    private val searchProgressBar by lazy { findViewById<ProgressBar>(R.id.searchProgressBar) }

    private val tracksRecycler by lazy { findViewById<RecyclerView>(R.id.tracksRecycler) }
    private val tracksNotFoundView by lazy { findViewById<View>(R.id.tracksNotFoundView) }
    private val tracksNetworkErrorView by lazy { findViewById<View>(R.id.tracksNetworkErrorView) }

    private val historyView by lazy { findViewById<View>(R.id.historyTracksView) }
    private val historyRecycler by lazy { findViewById<RecyclerView>(R.id.historyTracksRecycler) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        configureBackButton()
        configureSearchInput()
        configureUpdateButton()
        configureClearHistoryButton()

        configureTracksRecycler()
        configureHistoryRecycler()

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        viewModel.state.observe(this) { render(it) }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchHistory -> showHistoryList(state.tracks)
            is Loading -> showLoading()
            is SearchedTracks -> showSearchResult(state.tracks)

            is SearchState.SearchError -> {
                showErrorMessage(state.error)
            }
        }
    }

    private fun showHistoryList(tracks: List<Track>) {
        hideContent()
        historyAdapter.setTracks(tracks)
        if (tracks.isNotEmpty()) {
            historyView.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        hideContent()
        searchProgressBar.visibility = View.VISIBLE
    }

    private fun showSearchResult(tracks: List<Track>) {
        hideContent()
        tracksAdapter.clearAll()
        tracksAdapter.setTracks(tracks)
        tracksRecycler.visibility = View.VISIBLE
    }

    private fun showErrorMessage(error: String) {
        hideContent()
        when (error) {
            "Something went wrong" -> {

            }

            "No Internet connection" -> {

            }

//            NetworkError.EMPTY_RESULT -> {
//                binding.searchRecycler.visibility = View.GONE
//                binding.internetProblem.visibility = View.GONE
//                binding.searchHistoryLayout.visibility = View.GONE
//                binding.nothingFound.visibility = View.VISIBLE
//                binding.progressBar.visibility = View.GONE
//            }
//            NetworkError.CONNECTION_ERROR -> {
//                binding.searchRecycler.visibility = View.GONE
//                binding.nothingFound.visibility = View.GONE
//                binding.searchHistoryLayout.visibility = View.GONE
//                binding.internetProblem.visibility = View.VISIBLE
//                binding.progressBar.visibility = View.GONE
//            }
        }
    }

    private fun startPlayerActivity(track: Track) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(PlayerActivity.EXTRA_TRACK_KEY, track)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE_KEY, searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchValue = savedInstanceState.getString(SEARCH_VALUE_KEY, "")
        searchEditText.setText(searchValue)
    }

    private fun configureBackButton(): Unit = backButton.setOnClickListener { onBackPressed() }

    private fun configureSearchInput() {
        clearSearchButton.setOnClickListener { handleClearState() }
        searchEditText.run {
            doOnTextChanged { text, _, _, _ ->
                clearSearchButton.visibility = if (text.isNullOrEmpty()) GONE else VISIBLE
                text?.let { viewModel.searchDebounce(it.toString()) }
            }
            requestFocus()
        }
    }

    private fun configureUpdateButton(): Unit =
        updateButton.setOnClickListener {
            viewModel.search(searchInputQuery)
        }

    private fun configureClearHistoryButton(): Unit =
        clearHistoryButton.setOnClickListener {
            historyAdapter.clearAll()
            viewModel.clearHistory()
        }

    private fun configureTracksRecycler() {
        tracksRecycler.layoutManager = LinearLayoutManager(this)
        tracksRecycler.adapter = tracksAdapter
    }

    private fun configureHistoryRecycler() {
        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter
    }

    private fun hideKeyboard() {
        currentFocus?.let { view ->
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun handleClearState() {
        viewModel.clearSearchText()
        searchEditText.text.clear()
        hideContent()
        hideKeyboard()
    }

    private fun hideContent() = listOf(
        tracksRecycler, historyView, searchProgressBar, tracksNotFoundView, tracksNetworkErrorView
    ).forEach { it.isVisible = false }
}
