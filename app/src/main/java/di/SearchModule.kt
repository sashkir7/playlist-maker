package di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import data.common.network.NetworkClient
import data.common.network.RetrofitNetworkClient
import data.search.SearchRepository
import data.search.SearchRepositoryImpl
import domain.search.SearchInteractor
import domain.search.SearchInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ui.search.SearchViewModel

private const val HISTORY_SHARED_PREFS = "HISTORY"

val searchModule = module {

    viewModel<SearchViewModel> { SearchViewModel(get()) }

    single<SearchInteractor> { SearchInteractorImpl(get()) }

    single<SearchRepository> { SearchRepositoryImpl(get(), get()) }

    single<NetworkClient> { RetrofitNetworkClient(androidContext()) }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            HISTORY_SHARED_PREFS, MODE_PRIVATE
        )
    }
}
