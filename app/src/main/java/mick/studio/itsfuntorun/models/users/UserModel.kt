package mick.studio.itsfuntorun.models.users

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var uid: String ?= "",
    var registerDate: String ?= "",
    var name: String? = "",
    var image: String? = "",
    var password: String = "",
    var email: String = "john@doe.com"
    ) : Parcelable {
        @Exclude
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "uid" to uid,
                "registerDate" to registerDate,
                "name" to name,
                "email" to email,
                "image" to image,
                "password" to password
            )
        }
}
