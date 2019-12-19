package com.stolz.placessearch

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stolz.placessearch.model.Place

@BindingAdapter("typeaheadData")
fun bindTypeaheadRecyclerView(recyclerView: RecyclerView, data: Set<String>?) {
    if (data == null) {
        return
    }
    val adapter = recyclerView.adapter as TypeaheadResultsAdapter
    recyclerView.visibility = if (data.isEmpty()) View.GONE else View.VISIBLE
    adapter.submitList(data.toList())
}

@BindingAdapter("placeData")
fun bindRecyclerView(recyclerView: RecyclerView, data: Set<Place>?) {
    if (data == null) {
        return
    }
    val adapter = recyclerView.adapter as SearchResultsAdapter
    recyclerView.visibility = if (data.isEmpty()) View.GONE else View.VISIBLE
    adapter.submitList(data.toList())
}

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