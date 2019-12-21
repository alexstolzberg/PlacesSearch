package com.stolz.placessearch.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaceEntity::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun placeDao(): PlaceDao
}
