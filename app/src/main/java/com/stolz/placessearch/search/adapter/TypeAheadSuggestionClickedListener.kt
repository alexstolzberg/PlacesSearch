package com.stolz.placessearch.search.adapter

/**
 * This callback alerts the caller that a type ahead suggestion was selected
 */
interface TypeAheadSuggestionClickedListener {
    fun onSuggestionClicked(clickedSuggestion: String)
}
