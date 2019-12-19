package com.stolz.placessearch


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.stolz.placessearch.databinding.FragmentDetailBinding

private val TAG = DetailFragment::class.java

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = this

        // TODO: Should we show and hide the action bar
        (activity as AppCompatActivity).supportActionBar?.hide()

        val args = arguments ?: return null
        val safeArgs = DetailFragmentArgs.fromBundle(args)
        val place = safeArgs.place

        binding.contentDetails.name.text = place.name
        binding.contentDetails.category.text = place.category
        val distanceToCenterText = place.distanceToCenter.toString() + " miles to center"
        binding.contentDetails.distanceToCenter.text = distanceToCenterText
        binding.contentDetails.address.text = place.address

        // TODO: Google Map Image

        // TODO: Favorites support (in view model?) -- check state of fab

        // TODO: Get more contact info for venue

        binding.detailFab.setOnClickListener {
            // TODO: Toggle favorites
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO: Should we show and hide the action bar
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}
