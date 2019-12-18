package com.stolz.placessearch

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.stolz.placessearch.model.Place

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Place>?) {
    val adapter = recyclerView.adapter as SearchResultsAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.common_full_open_on_phone) // TODO: Placeholder and error image
                    .error(R.drawable.ic_launcher_background)
            )
            .into(imageView)
    }
}