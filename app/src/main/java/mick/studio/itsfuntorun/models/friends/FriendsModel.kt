package mick.studio.itsfuntorun.models.friends

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class FriendsModel(
    var uid: String? = "",
    var fid: String? = ""
    ) : Parcelable {
        @Exclude
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "uid" to uid,
                "fid" to fid
            )
        }
}
