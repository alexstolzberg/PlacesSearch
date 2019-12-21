package com.stolz.placessearch.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FavoritesDatabaseTest {

    private lateinit var placeDao: PlaceDao
    private lateinit var favoriteDatabase: FavoriteDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        favoriteDatabase = Room.inMemoryDatabaseBuilder(
            context, FavoriteDatabase::class.java
        ).build()
        placeDao = favoriteDatabase.placeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        favoriteDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_insertAndGetPlace() {
        val place = PlaceEntity(id = "1", name = "testPlace1")

        placeDao.insert(place)
        val placeFromDb = placeDao.getPlace("1")

        assertThat(placeFromDb?.name, equalTo("testPlace1"))
    }

    @Test
    @Throws(Exception::class)
    fun test_InsertAllAndGetAll() {
        val place1 = PlaceEntity(id = "1", name = "testPlace1")
        val place2 = PlaceEntity(id = "2", name = "testPlace2")
        val place3 = PlaceEntity(id = "3", name = "testPlace3")
        val placesList = listOf(place1, place2, place3)

        placeDao.insertAll(placesList)
        val placesListFromDb = placeDao.getAllPlaces()

        assertEquals(placesListFromDb.size, 3)
        assertEquals(placesListFromDb.get(0).name, "testPlace1")
        assertEquals(placesListFromDb.get(1).name, "testPlace2")
        assertEquals(placesListFromDb.get(2).name, "testPlace3")
    }

    @Test
    @Throws(Exception::class)
    fun test_updatePlace() {
        val place = PlaceEntity(id = "1", name = "testPlace1")

        placeDao.insert(place)
        val placeFromDb = placeDao.getPlace("1")

        placeFromDb?.name = "newTestPlace1"
        placeDao.updatePlace(placeFromDb)
        val newPlaceFromDb = placeDao.getPlace("1")

        assertEquals(newPlaceFromDb?.name, "newTestPlace1")
    }

    @Test
    @Throws(Exception::class)
    fun test_deletePlace() {
        val place = PlaceEntity(id = "1", name = "testPlace1")

        placeDao.insert(place)
        val placeFromDb = placeDao.getPlace("1")
        placeDao.delete(placeFromDb)
        val deletedPlace = placeDao.getPlace("1")

        assertNull(deletedPlace)
    }
}