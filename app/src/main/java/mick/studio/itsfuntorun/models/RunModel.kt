package mick.studio.itsfuntorun.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class RunModel(
    var uid: String? = "",
    var runid: String? = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var runTime: String? = "",
    var speed: Double? = 0.0,
    var distance: Double? = 0.0,
    var finishTime: String? = "",
    var amountOfCals: Int = 0,
    var image: String? = "",
    var zoom: Float = 15f,
    var email: String? = "joe@bloggs.com"
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "runid" to runid,
            "lat" to lat,
            "lng" to lng,
            "runTime" to runTime,
            "speed" to speed,
            "distance" to distance,
            "finishTime" to finishTime,
            "amountOfCals" to amountOfCals,
            "image" to image,
            "zoom" to zoom,
            "email" to email
        )
    }
}