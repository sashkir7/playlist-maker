package ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import creator.Creator

object SearchViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchHistoryViewModel(
            trackInteractor = Creator.provideTrackInteractor(),
            historyInteractor = Creator.provideHistoryInteractor()
        ) as T
    }
}
