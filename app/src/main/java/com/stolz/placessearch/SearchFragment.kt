package com.stolz.placessearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.stolz.placessearch.databinding.FragmentSearchBinding
import com.stolz.placessearch.model.Place

class SearchFragment : Fragment(), TypeAheadSuggestionClickedListener, PlaceClickedListener {

    private lateinit var binding: FragmentSearchBinding

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.lifecycleOwner = this

        (activity as AppCompatActivity).supportActionBar?.show()


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

        binding.typeaheadResultsList.adapter = TypeaheadResultsAdapter(this)
        binding.typeaheadResultsList.addItemDecoration(
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
        binding.searchResultsList.adapter = SearchResultsAdapter(this)

        binding.fab.setOnClickListener { view ->
            view.findNavController()
                .navigate(SearchFragmentDirections.actionSearchFragmentToMapFragment())
        }

        return binding.root
    }

    override fun onSuggestionClicked(clickedSuggestion: String) {
        searchViewModel.queryUpdated(clickedSuggestion)
        binding.searchBar.setQuery(clickedSuggestion, true)
        binding.typeaheadResultsList.visibility = View.GONE
    }

    override fun onPlaceClicked(clickedPlace: Place) {
        view?.findNavController()
            ?.navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(clickedPlace))
    }
}
