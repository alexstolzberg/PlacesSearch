package com.stolz.placessearch.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.stolz.placessearch.R
import com.stolz.placessearch.database.FavoriteDatabase
import com.stolz.placessearch.databinding.FragmentSearchBinding
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.util.Utils

private val TAG = SearchFragment::class.java.simpleName

class SearchFragment : Fragment(), TypeAheadSuggestionClickedListener, PlaceClickedListener {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search, container, false
        )
        binding.lifecycleOwner = this

        (activity as AppCompatActivity).supportActionBar?.show()

        // Initialize ViewModel
        val context = requireNotNull(context)
        val favoritesDatabase = FavoriteDatabase.getInstance(context).placeDao()
        val viewModelFactory = SearchViewModelFactory(favoritesDatabase)
        searchViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
        binding.viewModel = searchViewModel

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.typeaheadResultsList.visibility = View.GONE
                searchViewModel.getPlaces(query)
                Utils.hideSoftKeyboard(activity)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchViewModel.getTypeaheadResults(newText)
                return true
            }
        })

        binding.typeaheadResultsList.adapter =
            TypeaheadResultsAdapter(this)
        binding.typeaheadResultsList.addItemDecoration(
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
        searchViewModel.typeaheadResults.observe(this, Observer { typeAheadResults ->
            if (searchViewModel.status.value == SearchViewModel.SearchStatus.LOADING || typeAheadResults.isEmpty()) {
                binding.typeaheadResultsList.visibility = View.GONE
                Log.d(TAG, "Hiding typeahead results list")
            } else {
                val adapter = binding.typeaheadResultsList.adapter as TypeaheadResultsAdapter
                adapter.submitList(typeAheadResults.toList())
                binding.typeaheadResultsList.visibility = View.VISIBLE
                Log.d(TAG, "Showing and updating typeahead results list")
            }
        })


        binding.searchResultsList.adapter = SearchResultsAdapter(this)
        searchViewModel.places.observe(this, Observer { placeResults ->
            if (placeResults.isEmpty()) {
                binding.searchResultsList.visibility = View.GONE
                Log.d(TAG, "Hiding place results list")
            } else {
                val adapter = binding.searchResultsList.adapter as SearchResultsAdapter
                adapter.submitList(placeResults.toList())
                binding.searchResultsList.visibility = View.VISIBLE
                Log.d(TAG, "Showing and updating place results list")
            }
        })

        binding.fab.setOnClickListener { view ->
            view.findNavController()
                .navigate(
                    SearchFragmentDirections.actionSearchFragmentToMapFragment(
                        searchViewModel.places.value?.toTypedArray()
                    )
                )
        }

        return binding.root
    }

    override fun onSuggestionClicked(clickedSuggestion: String) {
        searchViewModel.updateQuery(clickedSuggestion)
        binding.searchBar.setQuery(clickedSuggestion, true)
        binding.typeaheadResultsList.visibility = View.GONE
    }

    override fun onPlaceClicked(clickedPlace: Place) {
        view?.findNavController()
            ?.navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(clickedPlace))
    }

    override fun onFavoriteClicked(clickedPlace: Place) {
        searchViewModel.updateFavoriteForPlace(clickedPlace)
    }
}