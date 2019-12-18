package com.stolz.placessearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stolz.placessearch.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSearchBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = searchViewModel

        binding.searchResultsList.adapter = SearchResultsAdapter()

        binding.fab.setOnClickListener { view ->
            view.findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToMapFragment())
        }

        return binding.root
    }
}
