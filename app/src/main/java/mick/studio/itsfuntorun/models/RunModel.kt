package mick.studio.itsfuntorun.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunModel(var id: Long = 0,
                    var runInKms: String = "",
                    var runInTime: String = "") : Parcelable
