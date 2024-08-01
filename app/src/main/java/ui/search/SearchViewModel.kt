package ui.search

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import creator.Creator
import domain.player.Track
import domain.search.SearchInteractor
import ui.search.SearchState.Loading
import ui.search.SearchState.SearchError
import ui.search.SearchState.SearchHistory
import ui.search.SearchState.SearchedTracks

class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel() {

    companion object {
        private val searchRequestToken = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L

        fun getViewModelFactory() = viewModelFactory {
            initializer { SearchViewModel(Creator.provideSearchInteractor()) }
        }
    }

    private var latestSearchText: String? = null
    private val handler = Handler(Looper.getMainLooper())

    private val mutableState = MutableLiveData<SearchState>()
    val state: LiveData<SearchState>
        get() = mutableState

    init {
        mutableState.postValue(SearchHistory(interactor.getHistory()))
    }

    fun search(expression: String) {
        if (expression.isEmpty()) return

        mutableState.postValue(Loading)
        interactor.searchTrack(expression, object : SearchInteractor.Consumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                if (errorMessage == null) {
                    mutableState.postValue(SearchedTracks(checkNotNull(foundTracks)))
                } else {
                    mutableState.postValue(SearchError(errorMessage))
                }
            }
        })
    }

    fun clearHistory() {
        interactor.clearAll()
        mutableState.postValue(SearchHistory(interactor.getHistory()))
    }

    fun clearSearchText() = mutableState.postValue(
        SearchHistory(interactor.getHistory())
    )

    fun addTrackToHistory(track: Track) {
        interactor.addTrack(track)
        mutableState.postValue(SearchHistory(interactor.getHistory()))
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) return

        latestSearchText = changedText
        handler.removeCallbacksAndMessages(searchRequestToken)

        val searchRunnable = Runnable { search(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(searchRunnable, searchRequestToken, postTime)
    }
}