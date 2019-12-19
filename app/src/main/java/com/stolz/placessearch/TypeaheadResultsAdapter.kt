package com.stolz.placessearch

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

private val TAG = TypeaheadResultsAdapter::class.java.simpleName

// TODO: USE DAGGER?
class TypeaheadResultsAdapter(private val itemClickedListener: TypeAheadSuggestionClickedListener) :
    ListAdapter<String, TypeaheadResultsAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_typeahead_search_result,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val typeaheadResult = getItem(position)
        holder.rowLayout.setOnClickListener {
            // TODO: IS THERE A BETTER WAY TO SEND THIS INFORMATION?
            Log.d(TAG, "Typeahead result clicked: ${holder.name}")
            itemClickedListener.onSuggestionClicked(typeaheadResult)
        }
        holder.name.text = typeaheadResult
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rowLayout: ConstraintLayout = itemView.findViewById(R.id.row_layout)
        val name: TextView = itemView.findViewById(R.id.name)
    }
}
