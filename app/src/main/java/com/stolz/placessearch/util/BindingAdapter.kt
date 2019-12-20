package com.stolz.placessearch.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stolz.placessearch.R
import com.stolz.placessearch.search.SearchViewModel

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

@BindingAdapter("fabStatus")
fun bindFabStatus(fab: FloatingActionButton, status: SearchViewModel.SearchStatus?) {
    when (status) {
        SearchViewModel.SearchStatus.LOADING -> fab.hide()
        SearchViewModel.SearchStatus.ERROR -> fab.hide()
        SearchViewModel.SearchStatus.DONE -> fab.show()
        SearchViewModel.SearchStatus.EMPTY -> fab.hide()
    }
}