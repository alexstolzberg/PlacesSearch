package com.stolz.placessearch.model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

class Place(
    val id: String?,
    val name: String?,
    val category: String?,
    val address: String?,
    val location: LatLng?,
    val distanceToCenter: Double,
    val iconUrl: String? = "",
    var isFavorite: Boolean = false
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(LatLng::class.java.classLoader),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(category)
        parcel.writeString(address)
        parcel.writeParcelable(location, flags)
        parcel.writeDouble(distanceToCenter)
        parcel.writeString(iconUrl)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents() = 0

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Place> {
            override fun createFromParcel(parcel: Parcel) = Place(parcel)

            override fun newArray(size: Int): Array<Place?> = arrayOfNulls(size)
        }
    }
}