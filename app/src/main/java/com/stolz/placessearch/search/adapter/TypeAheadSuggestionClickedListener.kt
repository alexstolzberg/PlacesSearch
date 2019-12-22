package com.stolz.placessearch.search.adapter

/**
 * This interface passes back information to the fragment when a typeahead suggestion is clicked
 */
interface TypeAheadSuggestionClickedListener {
    fun onSuggestionClicked(clickedSuggestion: String)
}
