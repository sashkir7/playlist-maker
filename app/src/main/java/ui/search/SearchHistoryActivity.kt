package ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import domain.player.Track
import ui.player.PlayerActivity
import utils.isVisible

class SearchHistoryActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_VALUE_KEY = "SEARCH_VALUE_KEY"
    }

    private lateinit var viewModel: SearchHistoryViewModel

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

    private val searchEditTextTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (searchEditText.hasFocus() && s?.isEmpty() == true) {
//                tracksRecycler.isVisible = false
//                historyView.isVisible = !viewModel.historyTracks.value.isNullOrEmpty()
            } else {
                viewModel.smartSearchTrack(searchEditText.text.toString())
            }
        }

        override fun afterTextChanged(s: Editable?) {
            clearSearchButton.isVisible = !s.isNullOrEmpty()
        }
    }

    private val searchFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus && searchEditText.text.isEmpty()) {
            historyView.isVisible = !viewModel.historyTracks.value.isNullOrEmpty()
        } else {
            historyView.isVisible = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(
            this,
            SearchViewModelFactory
        )[SearchHistoryViewModel::class.java]

        viewModel.isLoading.observe(this) { searchProgressBar.isVisible = it }
        viewModel.foundedTracks.observe(this) {
            if (it.isEmpty()) {
                tracksAdapter.clearAll()
                tracksRecycler.visibility = GONE
                tracksNotFoundView.visibility = VISIBLE
            } else {
                tracksAdapter.setTracks(it)
                tracksRecycler.visibility = VISIBLE
                tracksNotFoundView.visibility = GONE
                hideKeyboard()
            }
            tracksNetworkErrorView.visibility = GONE
            historyView.visibility = GONE
        }

        viewModel.historyTracks.observe(this) {
            if (it.isEmpty()) {
                historyAdapter.clearAll()
                historyView.visibility = GONE
            } else {
                historyView.visibility = VISIBLE
                historyAdapter.setTracks(it)
            }
            tracksNotFoundView.visibility = GONE
            tracksNetworkErrorView.visibility = GONE
        }

        configureBackButton()
        configureSearchInput()
        configureUpdateButton()
        configureClearHistoryButton()

        configureTracksRecycler()
        configureHistoryRecycler()
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
            addTextChangedListener(searchEditTextTextWatcher)
            setOnFocusChangeListener(searchFocusChangeListener)
            requestFocus()
        }
    }

    private fun configureUpdateButton(): Unit =
        updateButton.setOnClickListener {
//            searchInItunes()
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
        tracksRecycler.visibility = GONE
        tracksNotFoundView.visibility = GONE
        tracksNetworkErrorView.visibility = GONE

        searchEditText.text.clear()
        tracksAdapter.clearAll()

        hideKeyboard()
    }

    private fun handleNotEmptyTrackState(tracks: List<Track>) {
        tracksRecycler.visibility = VISIBLE
        tracksNotFoundView.visibility = GONE
        tracksNetworkErrorView.visibility = GONE
        historyView.visibility = GONE

        tracksAdapter.setTracks(tracks)
        hideKeyboard()
    }

    private fun handleEmptyTrackState() {
        tracksRecycler.visibility = GONE
        tracksNotFoundView.visibility = VISIBLE
        tracksNetworkErrorView.visibility = GONE
        historyView.visibility = GONE

        tracksAdapter.clearAll()
    }

    private fun handleNetworkErrorState() {
        tracksRecycler.visibility = GONE
        tracksNotFoundView.visibility = GONE
        tracksNetworkErrorView.visibility = VISIBLE
        historyView.visibility = GONE

        searchEditText.text.clear()
        tracksAdapter.clearAll()

        hideKeyboard()
    }

    private fun handleShowHistoryTracksState() {
        val tracks = viewModel.historyTracks.value
        if (tracks.isNullOrEmpty()) {
            historyView.visibility = GONE
        } else {
            historyView.visibility = VISIBLE
        }
    }
}
