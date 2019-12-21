package com.stolz.placessearch.details


import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.stolz.placessearch.R
import com.stolz.placessearch.databinding.FragmentDetailBinding
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.util.Utils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class DetailFragment : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        // Initialize ViewModel
        detailViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = detailViewModel

        (activity as AppCompatActivity).supportActionBar?.hide()

        val args = arguments ?: return null
        val safeArgs = DetailFragmentArgs.fromBundle(args)
        val place = safeArgs.place

        binding.contentDetails.name.text = place.name
        binding.contentDetails.category.text = place.category
        val distanceToCenterText = place.distanceToCenter.toString() + " miles to center"
        binding.contentDetails.distanceToCenter.text = distanceToCenterText
        binding.contentDetails.address.text = place.address


        binding.detailFab.setImageResource(if (place.isFavorite) R.drawable.ic_star_filled_white else R.drawable.ic_star_empty_white)
        binding.detailFab.setOnClickListener {
            place.isFavorite = !place.isFavorite
            binding.detailFab.setImageResource(if (place.isFavorite) R.drawable.ic_star_filled_white else R.drawable.ic_star_empty_white)
            detailViewModel.updateFavoriteForPlace(place)
        }

        detailViewModel.venueInformation.observe(this, Observer {
            updateContactInfo(it)
        })

        detailViewModel.staticMap.observe(this, Observer {
            binding.staticMapImage.setImageBitmap(it)
        })

        detailViewModel.getStaticMap(place)
        detailViewModel.getVenueInformation(place.id)

        return binding.root
    }

    private fun updateContactInfo(venue: Venue?) {
        // TODO: CLEAN THIS UP
        venue?.contact?.let { contactInfo ->
            if (contactInfo.phone != null && contactInfo.phone.isNotEmpty()) {
                binding.contentDetails.phone.text =
                    Utils.formatPhoneNumber(contactInfo.phone)
                Linkify.addLinks(
                    binding.contentDetails.phone, Patterns.PHONE, "tel:",
                    Linkify.sPhoneNumberMatchFilter, Linkify.sPhoneNumberTransformFilter
                )
                binding.contentDetails.phone.movementMethod = LinkMovementMethod.getInstance()
                binding.contentDetails.phone.visibility = View.VISIBLE
                binding.contentDetails.phoneTitle.visibility = View.VISIBLE
            }
        }

        venue?.url?.let { url ->
            if (url.isNotEmpty()) {
                binding.contentDetails.website.text = url
                binding.contentDetails.website.visibility = View.VISIBLE
                binding.contentDetails.websiteTitle.visibility = View.VISIBLE
            }
        }

        // TODO: Add more contact info
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}
