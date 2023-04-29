package mick.studio.itsfuntorun.ui.camera

import android.annotation.SuppressLint
import android.app.Application
import android.app.ProgressDialog
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.*

@SuppressLint("MissingPermission")
class ImagePickerViewModel(application: Application) : AndroidViewModel(application) {
    var image = MutableLiveData<String>()
    var storage: FirebaseStorage

    val observableImage: LiveData<String>
        get() = image

    init {
        storage = Firebase.storage

    }

    // on below line creating a function to upload our image.
    fun uploadImage(img: String) {
        val ref: StorageReference = FirebaseStorage.getInstance().getReference()
            .child(UUID.randomUUID().toString())
        val uploadTask = ref.putFile(Uri.parse(img))

        // on below line adding a file to our storage.
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                image.value = task.result.toString()
            } else {
                // Handle failures
                // ...
            }
        }
    }
}
