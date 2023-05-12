package mick.studio.itsfuntorun.models.fitness

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class FitnessModel(
    var name: String = "",
    var type: String = "",
    var muscle: String = "",
    var equipment: String = "",
    var difficulty: String = "",
    var instructions: String = ""
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "type" to type,
            "muscle" to muscle,
            "equipment" to equipment,
            "difficulty" to difficulty,
            "instructions" to instructions
        )
    }
}
