package com.stolz.placessearch.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlaceDao {
    @Insert
    fun insert(place: PlaceEntity)

    @Insert
    fun insertAll(places: List<PlaceEntity>)

    @Query("SELECT * FROM favorites_table WHERE id = :key")
    fun getPlace(key: String): LiveData<PlaceEntity>

    @Query("SELECT * FROM favorites_table")
    fun getAllPlaces(): LiveData<List<PlaceEntity>>


    @Query("SELECT * FROM favorites_table WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): LiveData<PlaceEntity>

    @Delete
    fun delete(user: PlaceEntity)
}
