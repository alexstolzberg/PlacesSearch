package com.stolz.placessearch.search

import com.nhaarman.mockitokotlin2.whenever
import com.stolz.placessearch.database.PlaceDao
import com.stolz.placessearch.model.Contact
import com.stolz.placessearch.model.Location
import com.stolz.placessearch.model.Meta
import com.stolz.placessearch.model.places.Object
import com.stolz.placessearch.model.places.Response
import com.stolz.placessearch.model.places.Venue
import com.stolz.placessearch.model.typeahead.Minivenue
import com.stolz.placessearch.network.FoursquareApiService
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.mock.Calls

class SearchRepositoryTest {

    @Mock
    lateinit var foursquareApiService: FoursquareApiService
    @Mock
    lateinit var favoriteDatabase: PlaceDao

    lateinit var searchRepository: SearchRepository

    private val testMinivenue1 = Minivenue(id = "1", name = "testMiniVenue1")
    private val testMinivenue2 = Minivenue(id = "2", name = "testMiniVenue2")

    private val testVenue1 = Venue(
        id = "1",
        name = "testVenue1",
        location = Location(1.0, 1.0),
        categories = ArrayList(),
        contact = Contact(),
        url = "www.test1.com"
    )

    private val testVenue2 = Venue(
        id = "2",
        name = "testVenue2",
        location = Location(2.0, 2.0),
        categories = ArrayList(),
        contact = Contact(),
        url = "www.test2.com"
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchRepository = SearchRepository(foursquareApiService, favoriteDatabase)

        val testTypeaheadResponse = com.stolz.placessearch.model.typeahead.Object(
            Meta(),
            com.stolz.placessearch.model.typeahead.Response(listOf(testMinivenue1, testMinivenue2))
        )
        whenever(foursquareApiService.getTypeaheadResults(query = "test")).thenReturn(
            Calls.response(testTypeaheadResponse)
        )

        val testPlacesResponse = Object(Meta(), Response(listOf(testVenue1, testVenue2)))
        whenever(foursquareApiService.getPlaces(query = "test")).thenReturn(
            Calls.response(testPlacesResponse)
        )
    }

    @Test
    fun test_fetchTypeaheadResults() = runBlocking {
        val typeaheadResults = searchRepository.fetchTypeaheadResults(query = "test")
        val resultsList = typeaheadResults.toList()

        assertNotNull(resultsList)
        assertTrue(resultsList.size == 2)
        assertEquals(resultsList[0], testMinivenue1.name)
        assertEquals(resultsList[1], testMinivenue2.name)
    }

    @Test
    fun test_fetchPlaces() = runBlocking {
        val venues = searchRepository.fetchPlaces(query = "test")
        val list = venues?.toList()

        assertNotNull(list)
        assertTrue(list?.size == 2)
        assertEquals(list?.get(0)?.name, testVenue1.name)
        assertEquals(list?.get(1)?.name, testVenue2.name)
    }

    @Test
    fun test_extractPlacesFromVenues_unique() {
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
        val venuesList = listOf(testVenue1, testVenue1)

        val result = searchRepository.extractPlacesFromVenues(venuesList)
        val list = result.toList()

        assertTrue(result.isNotEmpty())
        assertTrue(result.size == 1)
        assertEquals(list[0].name, testVenue1.name)
    }
}