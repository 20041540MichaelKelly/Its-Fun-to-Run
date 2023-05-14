package mick.studio.itsfuntorun.models.users

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var uid: String? = "",
    var pid: String? = "",
    var registerDate: String ?= "",
    var displayName: String? = "",
    var photoUrl: String? = "",
    var password: String = "",
    var email: String = "john@doe.com",
    var privateBio: Boolean = false
    ) : Parcelable {
        @Exclude
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "uid" to uid,
                "pid" to pid,
                "registerDate" to registerDate,
                "displayName" to displayName,
                "email" to email,
                "photoUrl" to photoUrl,
                "password" to password
            )
        }
}
