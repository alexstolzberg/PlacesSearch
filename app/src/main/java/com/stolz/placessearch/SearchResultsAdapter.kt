package com.stolz.placessearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stolz.placessearch.databinding.SearchResultItemBinding
import com.stolz.placessearch.model.Place

class SearchResultsAdapter :
    ListAdapter<Place, SearchResultsAdapter.PlaceViewHolder>(DiffCallback) {

    class PlaceViewHolder(private var binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            binding.place = place
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceViewHolder {
        return PlaceViewHolder(SearchResultItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }
}
