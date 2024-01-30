package activity

import adapter.TracksAdapter
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import api.dtos.TrackDto
import api.dtos.TracksResponseDto
import api.service.ITunesClient
import com.example.playlistmaker.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_VALUE_KEY = "SEARCH_VALUE_KEY"
    }

    private var lastSearchText = ""

    private val tracks: MutableList<TrackDto> = mutableListOf()
    private val tracksAdapter by lazy { TracksAdapter(tracks) }

    private val backButton by lazy { findViewById<ImageView>(R.id.backButton) }
    private val searchEditText by lazy { findViewById<EditText>(R.id.searchEditText) }
    private val clearSearchButton by lazy { findViewById<ImageView>(R.id.clearSearchButton) }
    private val updateButton by lazy { findViewById<Button>(R.id.updateButton) }

    private val tracksRecycler by lazy { findViewById<RecyclerView>(R.id.tracksRecycler) }
    private val tracksNotFoundView by lazy { findViewById<View>(R.id.tracksNotFoundView) }
    private val tracksNetworkErrorView by lazy { findViewById<View>(R.id.tracksNetworkErrorView) }

    private val searchEditTextTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            clearSearchButton.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
        }
    }

    private val searchTracksCallback = object : Callback<TracksResponseDto> {
        override fun onResponse(
            call: Call<TracksResponseDto>,
            response: Response<TracksResponseDto>
        ) {
            val tracks: List<TrackDto> = checkNotNull(response.body()).results
            if (tracks.isEmpty()) handleEmptyTrackState()
            else handleNotEmptyTrackState(tracks)
        }

        override fun onFailure(call: Call<TracksResponseDto>, t: Throwable) {
            handleNetworkErrorState()
        }
    }

    private val searchEditTextActionDoneListener =
        OnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE) false
            lastSearchText = searchEditText.text.toString()
            searchInItunes()
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        configureBackButton()
        configureSearchInput()
        configureUpdateButton()
        configureTracksRecycler()
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
        searchEditText.addTextChangedListener(searchEditTextTextWatcher)
        searchEditText.setOnEditorActionListener(searchEditTextActionDoneListener)
    }

    private fun configureUpdateButton() {
        updateButton.setOnClickListener { searchInItunes() }
    }

    private fun configureTracksRecycler() {
        tracksRecycler.layoutManager = LinearLayoutManager(this)
        tracksRecycler.adapter = tracksAdapter
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

        searchEditText.text.clear()
        tracks.clear()

        hideKeyboard()
    }

    private fun handleNotEmptyTrackState(tracks: List<TrackDto>) {
        tracksRecycler.visibility = VISIBLE
        tracksNotFoundView.visibility = GONE
        tracksNetworkErrorView.visibility = GONE

        this.tracks.clear()
        this.tracks.addAll(tracks)
        tracksAdapter.notifyDataSetChanged()

        hideKeyboard()
    }

    private fun handleEmptyTrackState() {
        tracksRecycler.visibility = GONE
        tracksNotFoundView.visibility = VISIBLE
        tracksNetworkErrorView.visibility = GONE

        tracks.clear()
    }

    private fun handleNetworkErrorState() {
        tracksRecycler.visibility = GONE
        tracksNotFoundView.visibility = GONE
        tracksNetworkErrorView.visibility = VISIBLE

        searchEditText.text.clear()
        tracks.clear()

        hideKeyboard()
    }
}
