package com.stolz.placessearch.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.stolz.placessearch.database.PlaceEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
class Place(
    val id: String = "",
    val name: String = "",
    val category: String? = null,
    val address: String? = null,
    val location: LatLng,
    val distanceToCenter: Double = 0.0,
    val iconUrl: String? = "",
    var isFavorite: Boolean = false
) : Parcelable {
    companion object {
        @JvmStatic
        fun toPlaceEntity(place: Place): PlaceEntity {
            val placeEntity = PlaceEntity()
            placeEntity.id = place.id
            placeEntity.name = place.name
            placeEntity.category = place.category
            placeEntity.latitude = place.location.latitude
            placeEntity.longitude = place.location.longitude
            placeEntity.distanceToCenter = place.distanceToCenter
            placeEntity.isFavorite = place.isFavorite
            return placeEntity
        }
    }
}