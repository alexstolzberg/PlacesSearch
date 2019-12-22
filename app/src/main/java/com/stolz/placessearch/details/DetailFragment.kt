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
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.util.TELEPHONE_PREFIX
import com.stolz.placessearch.util.Utils
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * This class handles all of the view related logic for the detail screen. It observes various data
 * from the DetailViewModel and updates the UI when new data arrives.
 */
class DetailFragment : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        // Initialize ViewModel
        detailViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = detailViewModel

        // Hide action bar on this fragment
        (activity as AppCompatActivity).supportActionBar?.hide()

        // Retrieve the place information that was passed in
        val args = arguments ?: return null
        val safeArgs = DetailFragmentArgs.fromBundle(args)
        val place = safeArgs.place

        initUi(place)

        // Create observer to add additional venue contact information when it is received
        detailViewModel.venueInformation.observe(this, Observer {
            if (it != null) {
                addAdditionalContactInfo(it)
            }
        })

        // Create observer to update static map image view when it is received
        detailViewModel.staticMap.observe(this, Observer {
            binding.staticMapImage.setImageBitmap(it)
        })

        // Retrieve static Google Map
        detailViewModel.getStaticMap(place)

        // Request additional venue information to populate more detailed contact info
        detailViewModel.getVenueInformation(place.id)

        return binding.root
    }

    private fun initUi(place: Place) {
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
    }

    /**
     * Adds additional contact info to a venue if it has that information
     *
     * @param venue An object containing more detailed info about a specific place
     */
    private fun addAdditionalContactInfo(venue: Venue) {
        venue.contact?.let { contactInfo ->
            if (contactInfo.phone != null && contactInfo.phone.isNotEmpty()) {
                binding.contentDetails.phone.text = Utils.formatPhoneNumber(contactInfo.phone)
                Linkify.addLinks(
                    binding.contentDetails.phone, Patterns.PHONE, TELEPHONE_PREFIX,
                    Linkify.sPhoneNumberMatchFilter, Linkify.sPhoneNumberTransformFilter
                )
                binding.contentDetails.phone.movementMethod = LinkMovementMethod.getInstance()
                binding.contentDetails.phone.visibility = View.VISIBLE
                binding.contentDetails.phoneTitle.visibility = View.VISIBLE
            }
        }

        venue.url?.let { url ->
            if (url.isNotEmpty()) {
                binding.contentDetails.website.text = url
                binding.contentDetails.website.visibility = View.VISIBLE
                binding.contentDetails.websiteTitle.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}
