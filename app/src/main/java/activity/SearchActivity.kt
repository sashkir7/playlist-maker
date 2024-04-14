package activity

import adapter.TracksAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.dtos.TrackDto
import api.dtos.TracksResponseDto
import api.service.ITunesClient
import com.example.playlistmaker.R
import storage.HISTORY_SHARED_PREFS
import storage.HistoryStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_VALUE_KEY = "SEARCH_VALUE_KEY"
        private const val DELAY = 2_000L
    }

    private var lastSearchText = ""
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private var isClickOnTrackDetailsAllowed = true

    private val historyStorage by lazy {
        HistoryStorage(getSharedPreferences(HISTORY_SHARED_PREFS, MODE_PRIVATE))
    }

    private val tracksAdapter by lazy {
        TracksAdapter { track ->
            OnClickListener {
                if (clickOnTrackDetailsDebounce()) {
                    historyStorage.addTrack(track)
                    startPlayerActivity(track)
                }
            }
        }
    }
    private val historyAdapter by lazy {
        TracksAdapter { track ->
            OnClickListener {
                if (clickOnTrackDetailsDebounce()) {
                    startPlayerActivity(track)
                }
            }
        }
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

    private fun startPlayerActivity(track: TrackDto) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(PlayerActivity.EXTRA_TRACK_KEY, track)
        startActivity(intent)
    }

    private val searchRunnable = Runnable { searchInItunes() }

    private val searchEditTextTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            mainThreadHandler.removeCallbacks(searchRunnable)

            if (searchEditText.hasFocus() && s?.isEmpty() == true) {
                handleShowHistoryTracksState()
            } else {
                tracksRecycler.visibility = GONE
                searchProgressBar.visibility = VISIBLE
                tracksNetworkErrorView.visibility = GONE
                tracksNotFoundView.visibility = GONE

                lastSearchText = searchEditText.text.toString()

                handleHideHistoryTracksState()
                mainThreadHandler.postDelayed(searchRunnable, DELAY)
            }
        }

        override fun afterTextChanged(s: Editable?) {
            clearSearchButton.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
        }
    }

    private val searchFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus && searchEditText.text.isEmpty()) {
            handleShowHistoryTracksState()
        } else {
            handleHideHistoryTracksState()
        }
    }

    private val searchTracksCallback = object : Callback<TracksResponseDto> {
        override fun onResponse(
            call: Call<TracksResponseDto>,
            response: Response<TracksResponseDto>
        ) {
            val tracks: List<TrackDto> = checkNotNull(response.body()).results
            if (tracks.isEmpty()) {
                handleEmptyTrackState()
            } else {
                handleNotEmptyTrackState(tracks)
            }
        }

        override fun onFailure(call: Call<TracksResponseDto>, t: Throwable) {
            handleNetworkErrorState()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        configureBackButton()
        configureSearchInput()
        configureUpdateButton()
        configureClearHistoryButton()

        configureTracksRecycler()
        configureHistoryRecycler()
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
        updateButton.setOnClickListener { searchInItunes() }

    private fun configureClearHistoryButton(): Unit =
        clearHistoryButton.setOnClickListener {
            historyAdapter.clearAll()
            historyStorage.clearAll()
            handleHideHistoryTracksState()
        }

    private fun configureTracksRecycler() {
        tracksRecycler.layoutManager = LinearLayoutManager(this)
        tracksRecycler.adapter = tracksAdapter
    }

    private fun configureHistoryRecycler() {
        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter
    }

    private fun searchInItunes(): Unit =
        ITunesClient.search(lastSearchText, searchTracksCallback)

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
        searchProgressBar.visibility = GONE

        searchEditText.text.clear()
        tracksAdapter.clearAll()

        hideKeyboard()
    }

    private fun handleNotEmptyTrackState(tracks: List<TrackDto>) {
        tracksRecycler.visibility = VISIBLE
        tracksNotFoundView.visibility = GONE
        tracksNetworkErrorView.visibility = GONE
        historyView.visibility = GONE
        searchProgressBar.visibility = GONE

        tracksAdapter.setTracks(tracks)
        hideKeyboard()
    }

    private fun handleEmptyTrackState() {
        tracksRecycler.visibility = GONE
        tracksNotFoundView.visibility = VISIBLE
        tracksNetworkErrorView.visibility = GONE
        historyView.visibility = GONE
        searchProgressBar.visibility = GONE

        tracksAdapter.clearAll()
    }

    private fun handleNetworkErrorState() {
        tracksRecycler.visibility = GONE
        tracksNotFoundView.visibility = GONE
        tracksNetworkErrorView.visibility = VISIBLE
        historyView.visibility = GONE
        searchProgressBar.visibility = GONE

        searchEditText.text.clear()
        tracksAdapter.clearAll()

        hideKeyboard()
    }

    private fun handleShowHistoryTracksState() {
        searchProgressBar.visibility = GONE
        val tracks = historyStorage.getHistory()
        if (tracks.isEmpty()) {
            historyView.visibility = GONE
        } else {
            historyView.visibility = VISIBLE
            historyAdapter.setTracks(tracks)
        }
    }

    private fun handleHideHistoryTracksState() {
        historyView.visibility = GONE
    }

    private fun clickOnTrackDetailsDebounce(): Boolean {
        val current = isClickOnTrackDetailsAllowed
        if (isClickOnTrackDetailsAllowed) {
            isClickOnTrackDetailsAllowed = false
            mainThreadHandler.postDelayed(
                { isClickOnTrackDetailsAllowed = true }, DELAY
            )
        }
        return current
    }
}
