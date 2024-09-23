package ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.player.Track
import domain.search.SearchInteractor
import kotlinx.coroutines.launch
import ui.search.SearchState.Loading
import ui.search.SearchState.SearchError
import ui.search.SearchState.SearchHistory
import ui.search.SearchState.SearchedTracks
import utils.debounce

class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

    private var latestSearchText: String? = null

    private val searchDebounceAction = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { changedText -> search(changedText) }

    private val mutableState = MutableLiveData<SearchState>()
    val state: LiveData<SearchState>
        get() = mutableState

    init {
        mutableState.postValue(SearchHistory(interactor.getHistory()))
    }

    fun search(expression: String) {
        if (expression.isEmpty()) return
        mutableState.postValue(Loading)

        viewModelScope.launch {
            interactor.searchTrack(expression)
                .collect { (foundTracks, errorMessage) ->
                    val state = if (errorMessage == null) {
                        SearchedTracks(checkNotNull(foundTracks))
                    } else { SearchError(errorMessage) }
                    mutableState.postValue(state)
                }
        }
    }

    fun clearHistory() {
        interactor.clearAll()
        mutableState.postValue(SearchHistory(interactor.getHistory()))
    }

    fun clearSearchText() = mutableState.postValue(
        SearchHistory(interactor.getHistory())
    )

    fun addTrackToHistory(track: Track) = interactor.addTrack(track)

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) return
        latestSearchText = changedText
        searchDebounceAction(changedText)
    }
}
