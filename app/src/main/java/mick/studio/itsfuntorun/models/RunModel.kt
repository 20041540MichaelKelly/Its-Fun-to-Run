package mick.studio.itsfuntorun.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunModel(var id: Long = getId(),
                    var runInKms: String = "",
                    var image: Uri = Uri.EMPTY,
                    var runInTime: String = "") : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
