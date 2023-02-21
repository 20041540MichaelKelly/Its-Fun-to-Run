package mick.studio.itsfuntorun.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RunModel(var id: Long = 0,
                    var runInKms: String = "",
                    var image: Uri = Uri.EMPTY,
                    var runInTime: String = "") : Parcelable
