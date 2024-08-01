package ui.search

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import domain.player.Track
import domain.search.HistoryInteractor
import domain.search.TrackInteractor

class SearchHistoryViewModel(
    private val trackInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor,
) : ViewModel() {

    companion object {
        private const val SEARCH_TRACKS_DELAY = 2_000L
    }

    private val isMutableLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = isMutableLoading

    private val mutableFoundedTracks = MutableLiveData<List<Track>>()
    val foundedTracks: LiveData<List<Track>>
        get() = mutableFoundedTracks

    private val mutableHistoryTracks = MutableLiveData<List<Track>>()
    val historyTracks: LiveData<List<Track>>
        get() = mutableHistoryTracks

    private val mutableLastSearchText = MutableLiveData("")


//        private val searchEditTextTextWatcher = object : TextWatcher {
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////            mainThreadHandler.removeCallbacks(searchRunnable)
//
//            if (searchEditText.hasFocus() && s?.isEmpty() == true) {
//                handleShowHistoryTracksState()
//            } else {
//                tracksRecycler.visibility = GONE
//                tracksNetworkErrorView.visibility = GONE
//                tracksNotFoundView.visibility = GONE
//
//                lastSearchText = searchEditText.text.toString()
//
//                handleHideHistoryTracksState()
//                mainThreadHandler.postDelayed(searchRunnable, DELAY)
//            }
//        }
//
//        override fun afterTextChanged(s: Editable?) {
//            clearSearchButton.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
//        }
//    }


    init {
        mutableHistoryTracks.postValue(historyInteractor.getHistory())
    }


    // ToDo А НАДО ЛИ?!
    // private var placeholderLiveData = MutableLiveData("")
    // fun getPlaceholderLiveData(): LiveData<String> = placeholderLiveData

    // ToDo А НАДО ЛИ?!
    // private var searchHistoryLiveData = MutableLiveData(false)
    // fun getSearchHistoryLiveData(): LiveData<Boolean> = searchHistoryLiveData


    private val mainThreadHandler = Handler(Looper.getMainLooper())


    private val runnable = Runnable { searchTrack123(mutableLastSearchText.value!!) }

    fun smartSearchTrack(expression: String) {
        isMutableLoading.postValue(true)
        mainThreadHandler.removeCallbacks(runnable)

        mutableLastSearchText.postValue(expression)
        mainThreadHandler.postDelayed(runnable, 2_000)

    }


    private fun searchTrack123(expression: String) {
        if (expression.isBlank()) return

//        isMutableLoading.postValue(true)
        mutableLastSearchText.postValue(expression)

        trackInteractor.searchTrack(expression, object : TrackInteractor.Consumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                mainThreadHandler.post {
                    isMutableLoading.postValue(false)
                    if (foundTracks == null) {
//                        Типа ошибка - надо обработать
//                        placeholderLiveData.postValue("no internet")
                    } else mutableFoundedTracks.postValue(foundTracks)
                }
            }
        })
    }

    fun addTrackToHistory(track: Track) {
        historyInteractor.addTrack(track)
        mutableHistoryTracks.postValue(historyInteractor.getHistory())
    }

    fun clearHistory() {
        historyInteractor.clearAll()
        mutableHistoryTracks.postValue(emptyList())
    }


//    fun searchHistoryVisible(visible: Boolean) {
//        searchHistoryLiveData.postValue(visible)
//    }
//
//    fun clear() {
//        tracksLiveData.postValue(ArrayList<TrackFromAPI>())
//    }


    fun searchDebounce(searchRunnable: Runnable) {
        mainThreadHandler.removeCallbacks(searchRunnable)
        mainThreadHandler.postDelayed(searchRunnable, SEARCH_TRACKS_DELAY)
    }

}
