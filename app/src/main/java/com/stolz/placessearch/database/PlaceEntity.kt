package com.stolz.placessearch.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class PlaceEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "category")
    var category: String = "",

    @ColumnInfo(name = "latitude")
    var latitude: Double = 0.0,

    @ColumnInfo(name = "longitude")
    var longitude: Double = 0.0,

    @ColumnInfo(name = "distanceToCenter")
    var distanceToCenter: Double = 0.0,

    @ColumnInfo(name = "iconUrl")
    var iconUrl: String = "",

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)
