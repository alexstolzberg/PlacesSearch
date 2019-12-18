package com.stolz.placessearch

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stolz.placessearch.model.Place

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: Set<Place>?) {
    val adapter = recyclerView.adapter as SearchResultsAdapter
    adapter.submitList(data?.toList())
}

//@BindingAdapter("imageUrl")
//fun bindImage(imageView: ImageView, imgUrl: String?) {
//    imgUrl?.let {
//        val imgUri = it.toUri().buildUpon().scheme("https").build()
//        Glide.with(imageView.context)
//            .load(imgUri)
//            .apply(
//                RequestOptions()
//                    .placeholder(R.drawable.loading_spinner)
//                    .error(R.drawable.ic_connection_error)
//            )
//            .into(imageView)
//    }
//}

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
        SearchViewModel.SearchStatus.DONE -> {
            searchStatusImageView.visibility = View.GONE
        }
    }
}