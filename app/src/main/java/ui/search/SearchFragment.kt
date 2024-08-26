package ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchBinding
import domain.player.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import ui.player.PlayerActivity
import ui.search.SearchState.Loading
import ui.search.SearchState.SearchHistory
import ui.search.SearchState.SearchedTracks
import utils.isVisible

class SearchFragment : Fragment() {

    companion object {
        private const val SEARCH_VALUE_KEY = "SEARCH_VALUE_KEY"
    }

    private var searchInputQuery = ""
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureSearchInput()
        configureUpdateButton()
        configureClearHistoryButton()

        configureTracksRecycler()
        configureHistoryRecycler()

        viewModel.state.observe(viewLifecycleOwner) { render(it) }
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchHistory -> showHistoryList(state.tracks)
            is Loading -> showLoading()
            is SearchedTracks -> showSearchResult(state.tracks)
            is SearchState.SearchError -> showErrorMessage()
        }
    }

    private fun showHistoryList(tracks: List<Track>) {
        hideContent()
        historyAdapter.setTracks(tracks)
        if (tracks.isNotEmpty()) {
            binding.historyTracksView.isVisible = true
        }
    }

    private fun showLoading() {
        hideContent()
        binding.searchProgressBar.isVisible = true
    }

    private fun showSearchResult(tracks: List<Track>) {
        hideContent()
        tracksAdapter.clearAll()
        tracksAdapter.setTracks(tracks)
        binding.tracksRecycler.isVisible = true
    }

    private fun showErrorMessage() {
        hideContent()
        binding.tracksNetworkErrorView.isVisible = true
    }

    private fun startPlayerActivity(track: Track) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java)
        intent.putExtra(PlayerActivity.EXTRA_TRACK_KEY, track)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE_KEY, binding.searchEditText.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val searchValue = savedInstanceState?.getString(SEARCH_VALUE_KEY) ?: ""
        binding.searchEditText.setText(searchValue)
    }

    private fun configureSearchInput() {
        val clearSearchButton = binding.clearSearchButton
        clearSearchButton.setOnClickListener { handleClearState() }
        binding.searchEditText.run {
            doOnTextChanged { text, _, _, _ ->
                clearSearchButton.isVisible = if (text.isNullOrEmpty()) false else true
                text?.let { viewModel.searchDebounce(it.toString()) }
            }
        }
    }

    private fun configureUpdateButton() =
        binding.updateButton.setOnClickListener { viewModel.search(searchInputQuery) }

    private fun configureClearHistoryButton() =
        binding.clearHistoryButton.setOnClickListener {
            historyAdapter.clearAll()
            viewModel.clearHistory()
        }

    private fun configureTracksRecycler() = with(binding.tracksRecycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = tracksAdapter
    }

    private fun configureHistoryRecycler() = with(binding.historyTracksRecycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = historyAdapter
    }

    private fun hideKeyboard() = with(requireActivity()) {
        currentFocus?.let { view ->
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun handleClearState() {
        viewModel.clearSearchText()
        binding.searchEditText.text.clear()
        hideContent()
        hideKeyboard()
    }

    private fun hideContent() = listOf(
        binding.tracksRecycler,
        binding.historyTracksView,
        binding.searchProgressBar,
        binding.tracksNotFoundView,
        binding.tracksNetworkErrorView
    ).forEach { it.isVisible = false }
}
