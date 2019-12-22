package com.stolz.placessearch.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stolz.placessearch.R
import com.stolz.placessearch.search.SearchViewModel

/**
 * This binding adapter toggles the visibility of the search status icon based on the LiveData from
 * the SearchViewModel. A spinner is shown in the loading state and a connection error icon is
 * shown in the even of an error. Nothing shows if a search is successful.
 */
@BindingAdapter("searchStatus")
fun bindSearchStatus(searchStatusImageView: ImageView, status: SearchViewModel.SearchStatus?) {
    when (status) {
        SearchViewModel.SearchStatus.LOADING -> {
            searchStatusImageView.visibility = View.VISIBLE
            searchStatusImageView.setImageResource(R.drawable.loading_spinner)
        }
        SearchViewModel.SearchStatus.ERROR -> {
            searchStatusImageView.visibility = View.VISIBLE
            searchStatusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        SearchViewModel.SearchStatus.DONE -> searchStatusImageView.visibility = View.GONE
        SearchViewModel.SearchStatus.EMPTY -> searchStatusImageView.visibility = View.GONE
    }
}

/**
 * This binding adapter toggles the visibility of the fab based on the LiveData from
 * the SearchViewModel. The fab is only shown when a search has completed and has results
 */
@BindingAdapter("fabStatus")
fun bindFabStatus(fab: FloatingActionButton, status: SearchViewModel.SearchStatus?) {
    when (status) {
        SearchViewModel.SearchStatus.LOADING -> fab.hide()
        SearchViewModel.SearchStatus.ERROR -> fab.hide()
        SearchViewModel.SearchStatus.DONE -> fab.show()
        SearchViewModel.SearchStatus.EMPTY -> fab.hide()
    }
}