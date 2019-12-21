package com.stolz.placessearch.database

import androidx.room.*

@Dao
interface PlaceDao {
    @Insert
    fun insert(place: PlaceEntity)

    @Insert
    fun insertAll(places: List<PlaceEntity>)

    @Update
    fun updatePlace(place: PlaceEntity?)

    @Query("SELECT * FROM favorites_table WHERE id = :key")
    fun getPlace(key: String): PlaceEntity?

    @Query("SELECT * FROM favorites_table")
    fun getAllPlaces(): List<PlaceEntity>

    @Delete
    fun delete(place: PlaceEntity?)
}
