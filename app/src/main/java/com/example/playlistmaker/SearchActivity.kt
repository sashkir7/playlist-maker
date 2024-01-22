package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_VALUE_KEY = "SEARCH_VALUE_KEY"
    }

    private var searchValue = ""

    private val backButton by lazy { findViewById<ImageView>(R.id.backButton) }
    private val searchEditText by lazy { findViewById<EditText>(R.id.searchEditText) }
    private val clearSearchButton by lazy { findViewById<ImageView>(R.id.clearSearchButton) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        configureBackButton()
        configureSearchInput()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE_KEY, searchValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchValue = savedInstanceState.getString(SEARCH_VALUE_KEY, "")
        searchEditText.setText(searchValue)
    }

    private fun configureBackButton() {
        backButton.setOnClickListener { onBackPressed() }
    }

    private fun configureSearchInput() {
        clearSearchButton.setOnClickListener {
            searchEditText.text.clear()
            hideKeyboard()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                clearSearchButton.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
                searchValue = s.toString()
            }
        }
        searchEditText.addTextChangedListener(textWatcher)
    }

    private fun hideKeyboard() {
        currentFocus?.let { view ->
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
