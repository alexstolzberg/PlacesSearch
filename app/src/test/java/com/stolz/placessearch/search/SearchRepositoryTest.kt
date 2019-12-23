package com.stolz.placessearch.search

import com.stolz.placessearch.database.PlaceDao
import com.stolz.placessearch.model.Contact
import com.stolz.placessearch.model.Location
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.network.FoursquareApiService
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SearchRepositoryTest {

    @Mock
    lateinit var foursquareApiService: FoursquareApiService
    @Mock
    lateinit var favoriteDatabase: PlaceDao

    lateinit var searchRepository: SearchRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchRepository = SearchRepository(foursquareApiService, favoriteDatabase)
    }

    @Test
    fun test_extractPlacesFromVenues_unique() {
        val testVenue1 = Venue(
            id = "1",
            name = "testVenue1",
            location = Location(1.0, 1.0),
            categories = ArrayList(),
            contact = Contact(),
            url = "www.test1.com"
        )

        val testVenue2 = Venue(
            id = "2",
            name = "testVenue2",
            location = Location(2.0, 2.0),
            categories = ArrayList(),
            contact = Contact(),
            url = "www.test2.com"
        )

        val venuesList = listOf(testVenue1, testVenue2)

        val result = searchRepository.extractPlacesFromVenues(venuesList)
        assertTrue(result.isNotEmpty())
        val list = result.toList()

        assertTrue(result.size == 2)
        assertEquals(list[0].name, testVenue1.name)
        assertEquals(list[1].name, testVenue2.name)
    }

    @Test
    fun test_extractPlacesFromVenues_duplicate() {
        val testVenue1 = Venue(
            id = "1",
            name = "testVenue1",
            location = Location(1.0, 1.0),
            categories = ArrayList(),
            contact = Contact(),
            url = "www.test1.com"
        )

        val venuesList = listOf(testVenue1, testVenue1)

        val result = searchRepository.extractPlacesFromVenues(venuesList)
        val list = result.toList()

        assertTrue(result.isNotEmpty())
        assertTrue(result.size == 1)
        assertEquals(list[0].name, testVenue1.name)
    }
}