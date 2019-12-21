package com.stolz.placessearch.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.stolz.placessearch.R
import com.stolz.placessearch.model.Place

private val TAG = SearchResultsAdapter::class.java.simpleName

class SearchResultsAdapter(private val listener: PlaceClickedListener) :
    ListAdapter<Place, SearchResultsAdapter.ViewHolder>(DiffCallback) {

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
                R.layout.row_place_search_result,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = getItem(position)

        holder.rowLayout.setOnClickListener {
            Log.d(TAG, "Place result clicked: ${holder.name}")
            listener.onPlaceClicked(place)
        }

        holder.name.text = place.name
        Glide.with(holder.icon.context)
            .load(place.iconUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_broken_image)
            )
            .into(holder.icon)
        holder.category.text = place.category
        val distanceFromCenterText = place.distanceToCenter.toString() + " miles from center"
        holder.distanceToCenter.text = distanceFromCenterText

        updateFavoriteIcon(place, holder)
        holder.favorite.setOnClickListener {
            place.isFavorite = !place.isFavorite
            updateFavoriteIcon(place, holder)
            listener.onFavoriteClicked(place)
        }
    }

    /**
     * Updates the star/favorite icon based on the current places isFavorite field
     */
    private fun updateFavoriteIcon(place: Place, holder: ViewHolder) {
        if (place.isFavorite) {
            holder.favorite.setImageResource(R.drawable.ic_star_filled)
        } else {
            holder.favorite.setImageResource(R.drawable.ic_star_empty)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rowLayout: CardView = itemView.findViewById(R.id.row_layout)
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val name: TextView = itemView.findViewById(R.id.name)
        val category: TextView = itemView.findViewById(R.id.category)
        val distanceToCenter: TextView = itemView.findViewById(R.id.distance)
        val favorite: ImageView = itemView.findViewById(R.id.favorite)
    }
}
