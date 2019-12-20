package com.stolz.placessearch.search

/**
 * This callback alerts the caller that a type ahead suggestion was selected
 */
interface TypeAheadSuggestionClickedListener {
    fun onSuggestionClicked(clickedSuggestion: String)
}
