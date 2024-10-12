package ui.media.favoriteTracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.player.FavoriteInteractor
import domain.player.Track
import kotlinx.coroutines.launch
import ui.media.favoriteTracks.FavoriteTracksState.Content
import ui.media.favoriteTracks.FavoriteTracksState.Empty

class FavoriteTracksViewModel(
    private val interactor: FavoriteInteractor
) : ViewModel() {

    private val mutableState = MutableLiveData<FavoriteTracksState>()
    val state: LiveData<FavoriteTracksState> get() = mutableState

    fun fillData() = viewModelScope.launch {
        interactor.getAll().collect { tracks -> processResult(tracks) }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            mutableState.postValue(Empty)
        } else {
            mutableState.postValue(Content(tracks))
        }
    }
}
