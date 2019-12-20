package com.stolz.placessearch.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlaceDao {
    @Insert
    fun insert(place: PlaceEntity)

    @Insert
    fun insertAll(places: List<PlaceEntity>)

    @Update
    fun updatePlace(place: PlaceEntity)

    @Query("SELECT * FROM favorites_table WHERE id = :key")
    fun getPlace(key: String): PlaceEntity?

    @Query("SELECT * FROM favorites_table")
    fun getAllPlaces(): LiveData<List<PlaceEntity>>

    @Delete
    fun delete(user: PlaceEntity)
}
