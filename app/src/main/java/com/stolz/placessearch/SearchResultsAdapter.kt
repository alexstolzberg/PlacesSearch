package com.stolz.placessearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.stolz.placessearch.model.Place

class SearchResultsAdapter : ListAdapter<Place, SearchResultsAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_result_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = getItem(position)
        holder.name.text = place.name
        Glide.with(holder.icon.context)
            .load(place.iconUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_spinner)
                    .error(R.drawable.ic_connection_error)
            )
            .into(holder.icon)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val name: TextView = itemView.findViewById(R.id.name)
        // TODO: ADD OTHER ITEMS
    }
}
