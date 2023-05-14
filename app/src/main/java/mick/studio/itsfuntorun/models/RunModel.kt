package mick.studio.itsfuntorun.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class RunModel(
    var uid: String ?= "",
    var runid: String ?= "",
    var lat: Double ?= 0.0,
    var lng: Double ?= 0.0,
    var runTime: String ?= "",
    var displayName: String? = "",
    var speed: Double ?= 0.0,
    var distance: Double ?= 0.0,
    var finishTime: String ?= "",
    var amountOfCals: Double = 0.0,
    var photoUrl: String? = "",
    var zoom: Float = 15f,
    var comment: String = ""
    ) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "runid" to runid,
            "lat" to lat,
            "lng" to lng,
            "runTime" to runTime,
            "displayName" to displayName,
            "speed" to speed,
            "distance" to distance,
            "finishTime" to finishTime,
            "amountOfCals" to amountOfCals,
            "photoUrl" to photoUrl,
            "zoom" to zoom,
            "comment" to comment,
        )
    }
}