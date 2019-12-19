package com.stolz.placessearch

/**
 * This callback alerts the caller that a type ahead suggestion was selected
 */
interface TypeAheadSuggestionClickedListener {
    fun onSuggestionClicked(clickedSuggestion: String)
}
