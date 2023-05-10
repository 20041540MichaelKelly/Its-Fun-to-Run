package mick.studio.itsfuntorun.ui.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.ActivityRegisterBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.showImagePicker
import mick.studio.itsfuntorun.models.users.UserModel
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Register : AppCompatActivity() {

    private lateinit var loginRegisterViewModel: LoginRegisterViewModel
    private lateinit var loggedInViewModel: LoggedInViewModel
    private lateinit var registerBinding: ActivityRegisterBinding
    lateinit var loader: AlertDialog
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    var user = UserModel()
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        loader = createLoader(this)
        registerImagePickerCallback()
        setButtonOnClickListeners(registerBinding)

        registerBinding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerBinding.captureImage.setOnClickListener {
            dispatchTakePictureIntent()
        }


//        registerBinding.registerEmail.setOnClickListener {
//            signIn(registerBinding.fieldEmail.text.toString(),
//                registerBinding.fieldPassword.text.toString())
//        }
//        registerBinding.emailCreateAccountButton.setOnClickListener {
//            createAccount(registerBinding.fieldEmail.text.toString(),
//                registerBinding.fieldPassword.text.toString())
//        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)

        //        loginRegisterViewModel.liveFirebaseUser.observe(this, Observer
//        { firebaseUser -> if (firebaseUser != null)
//            hideLoader(loader)
//            startActivity(Intent(this, Home::class.java)) })
//
//        loginRegisterViewModel.firebaseAuthManager.errorStatus.observe(this, Observer
//        { status -> checkStatus(status) })
    }

    //Required to exit app from Login Screen - must investigate this further
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Click again to Close App...", Toast.LENGTH_LONG).show()
        finish()
    }

//    private fun createAccount(email: String, password: String) {
//        Timber.d("createAccount:$email")
//        if (!validateForm()) { return }
//
//        loginRegisterViewModel.register(email,password)
//    }
//
//    private fun signIn(email: String, password: String) {
//        Timber.d("signIn:$email")
//        if (!validateForm()) { return }
//
//        showLoader(loader,"Signing in...")
//        loginRegisterViewModel.login(email,password)
//    }

    private fun checkStatus(error: Boolean) {
        if (error)
            Toast.makeText(
                this,
                getString(R.string.auth_failed),
                Toast.LENGTH_LONG
            ).show()
    }

//    private fun validateForm(): Boolean {
//        var valid = true
//
//        val email = registerBinding.fieldEmail.text.toString()
//        if (TextUtils.isEmpty(email)) {
//            registerBinding.fieldEmail.error = "Required."
//            valid = false
//        } else {
//            registerBinding.fieldEmail.error = null
//        }
//
//        val password = registerBinding.fieldPassword.text.toString()
//        if (TextUtils.isEmpty(password)) {
//            registerBinding.fieldPassword.error = "Required."
//            valid = false
//        } else {
//            registerBinding.fieldPassword.error = null
//        }
//        return valid
//    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            user.image = result.data!!.data!!.toString()
                            Picasso.get()
                                .load(user.image)
                                .into(registerBinding.runImage)

                            registerBinding.chooseImage.setText(R.string.change_image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun setButtonOnClickListeners(
        layout: ActivityRegisterBinding
    ) {
        layout.btnRegister.setOnClickListener() {
            user.name = layout.registerFullName.text.toString()
            user.email = layout.registerEmail.text.toString()
            user.registerDate =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
            user.password = layout.registerPassword.text.toString()
            if (user.password != "" || user.email != "") {
                loginRegisterViewModel.registerUserCreate(
                    UserModel(
                        name = user.name,
                        password = user.password,
                        image = user.image,
                        registerDate = user.registerDate,
                        email = user.email
                    )
                )
            } else {
                Snackbar
                    .make(
                        registerBinding.btnRegister,
                        getString(R.string.enter_a_user_info),
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }


            layout.chooseImage.setOnClickListener {
                showImagePicker(imageIntentLauncher)
            }

            layout.captureImage.setOnClickListener {
                dispatchTakePictureIntent()
            }
    }

}

@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
private fun createImageFile(): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = absolutePath
    }
}

private fun dispatchTakePictureIntent() {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        // Ensure that there's a camera activity to handle the intent

        takePictureIntent.resolveActivity(this.packageManager)?.also {
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                Timber.i("Take Picture Error : $ex.message")
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "mick.studio.itsfuntorun.fileprovider",
                    it
                )
                user.image = photoURI.toString()
                Picasso.get()
                    .load(user.image)
                    .into(registerBinding.runImage)

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

}
}

