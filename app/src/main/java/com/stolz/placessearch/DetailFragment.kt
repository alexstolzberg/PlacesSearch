package com.stolz.placessearch


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
import androidx.lifecycle.ViewModelProviders
import com.stolz.placessearch.databinding.FragmentDetailBinding
import com.stolz.placessearch.model.places.Venue

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private val detailViewModel: DetailViewModel by lazy {
        ViewModelProviders.of(this).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
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

        // TODO: Favorites support (in view model?) -- check state of fab
        binding.detailFab.setOnClickListener {
            // TODO: Toggle favorites
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
                binding.contentDetails.phone.text = Utils.formatPhoneNumber(contactInfo.phone)
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
        // TODO: Should we show and hide the action bar
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}
