package com.stolz.placessearch.details

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.whenever
import com.stolz.placessearch.model.Contact
import com.stolz.placessearch.model.Location
import com.stolz.placessearch.model.Meta
import com.stolz.placessearch.model.Place
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.network.FoursquareApiService
import com.stolz.placessearch.network.GoogleMapsApiService
import com.stolz.placessearch.util.BitmapUtils
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.mock.Calls

class DetailRepositoryTest {

    @Mock
    lateinit var mockFoursquareApiService: FoursquareApiService
    @Mock
    lateinit var mockGoogleMapsApiService: GoogleMapsApiService
    @Mock
    lateinit var mockBitmapUtils: BitmapUtils
    @Mock
    lateinit var mockResponseBody: ResponseBody

    lateinit var detailRepository: DetailRepository

    private val testBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)

    private val testPlace = Place(location = LatLng(1.0, 1.0))

    private val testVenue1 = Venue(
        id = "1",
        name = "testVenue1",
        location = Location(1.0, 1.0),
        categories = ArrayList(),
        contact = Contact(),
        url = "www.test1.com"
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        detailRepository =
            DetailRepository(mockFoursquareApiService, mockGoogleMapsApiService, mockBitmapUtils)

        whenever(mockGoogleMapsApiService.getStaticMap()).thenReturn(
            Calls.response(mockResponseBody)
        )
        whenever(mockBitmapUtils.createBitmap(mockResponseBody)).thenReturn(testBitmap)

        val testVenueInfoResponse = com.stolz.placessearch.model.venue_information.Object(
            Meta(),
            com.stolz.placessearch.model.venue_information.Response(testVenue1)
        )
        whenever(mockFoursquareApiService.getVenueDetails(venueId = "test")).thenReturn(
            Calls.response(testVenueInfoResponse)
        )
    }

    @Test
    fun test_fetchStaticBitmap() = runBlocking {
        val bitmap = detailRepository.fetchStaticMap(testPlace)

        assertEquals(testBitmap, bitmap)
    }

    @Test
    fun test_fetchVenueInformation() = runBlocking {
        val venueInformation = detailRepository.fetchVenueInformation(venueId = "test")

        assertNotNull(venueInformation)
        assertEquals(venueInformation?.id, testVenue1.id)
        assertEquals(venueInformation?.name, testVenue1.name)
        assertEquals(venueInformation?.location, testVenue1.location)
        assertEquals(venueInformation?.url, testVenue1.url)
    }
}