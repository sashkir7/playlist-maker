package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        configureSearchInput()
    }

    private fun configureSearchInput() {
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val clearSearchButton = findViewById<ImageView>(R.id.clearSearchButton)

        clearSearchButton.setOnClickListener {
            searchEditText.text.clear()
            hideKeyboard()
        }

        val textWatcher = searchEditTextTextWatcher(clearSearchButton)
        searchEditText.addTextChangedListener(textWatcher)
    }

    private fun hideKeyboard() {
        currentFocus?.let { view ->
            (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun searchEditTextTextWatcher(hideView: View): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
            hideView.visibility = visibility
        }
    }
}
