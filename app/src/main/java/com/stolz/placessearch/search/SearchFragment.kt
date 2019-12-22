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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.stolz.placessearch.R
import com.stolz.placessearch.databinding.FragmentSearchBinding
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.search.adapter.PlaceClickedListener
import com.stolz.placessearch.search.adapter.SearchResultsAdapter
import com.stolz.placessearch.search.adapter.TypeAheadSuggestionClickedListener
import com.stolz.placessearch.search.adapter.TypeaheadResultsAdapter
import com.stolz.placessearch.util.Utils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

private val TAG = SearchFragment::class.java.simpleName

/**
 * This class handles all of the view related logic for the search screen. It observes various data
 * from the SearchViewModel and updates the UI when new data arrives.
 */
class SearchFragment : Fragment(),
    TypeAheadSuggestionClickedListener,
    PlaceClickedListener {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        // Initialize ViewModel
        searchViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

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
        binding.viewModel = searchViewModel

        (activity as AppCompatActivity).supportActionBar?.show()

        binding.resultsSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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


        binding.searchResultsList.adapter =
            SearchResultsAdapter(this)
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

        searchViewModel.status.observe(this, Observer {
            if (it == SearchViewModel.SearchStatus.ERROR) {
                Snackbar.make(binding.parentLayout, "No Internet Connection", Snackbar.LENGTH_LONG)
                    .show()
            }
        })

        return binding.root
    }

    override fun onSuggestionClicked(clickedSuggestion: String) {
        searchViewModel.lastQuery = clickedSuggestion
        binding.resultsSearchBar.setQuery(clickedSuggestion, true)
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
