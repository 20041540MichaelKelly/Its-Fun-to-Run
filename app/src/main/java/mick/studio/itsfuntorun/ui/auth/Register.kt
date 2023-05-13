package mick.studio.itsfuntorun.ui.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.ActivityRegisterBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.hideLoader
import mick.studio.itsfuntorun.helpers.showImagePicker
import mick.studio.itsfuntorun.helpers.showLoader
import mick.studio.itsfuntorun.models.SharedViewModel
import mick.studio.itsfuntorun.models.users.UserModel
import mick.studio.itsfuntorun.ui.camera.ImagePickerViewModel
import mick.studio.itsfuntorun.ui.home.Home
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Register : AppCompatActivity() {

    private lateinit var loginRegisterViewModel: LoginRegisterViewModel
    private lateinit var loggedInViewModel: LoggedInViewModel
    private lateinit var registerBinding: ActivityRegisterBinding
    lateinit var loader: AlertDialog
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var imagePickerViewModel: ImagePickerViewModel
    var user = UserModel()
    private lateinit var sharedViewModel: SharedViewModel

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


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        imagePickerViewModel = ViewModelProvider(this).get(ImagePickerViewModel::class.java)

        loginRegisterViewModel.liveFirebaseUser.observe(
            this,
            androidx.lifecycle.Observer { firebaseUser ->
                if (firebaseUser != null)
                    hideLoader(loader)
                startActivity(Intent(this, Home::class.java))
            })

        loginRegisterViewModel.firebaseAuthManager.errorStatus.observe(
            this,
            androidx.lifecycle.Observer { status -> checkStatus(status) })
    }


    //Required to exit app from Login Screen - must investigate this further
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Click again to Close App...", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun createAccount(email: String, password: String, name: String) {
        Timber.d("createAccount:$email")
        if (!validateForm()) {
            Snackbar
                .make(
                    registerBinding.btnRegister,
                    getString(R.string.enter_a_user_info),
                    Snackbar.LENGTH_LONG
                )
                .show()

            return
        }
        loginRegisterViewModel.registerUserCreate(
            UserModel(
                displayName = name,
                password = password,
                photoUrl = user.photoUrl,
                registerDate = user.registerDate,
                email = email
            )
        )

    }

    private fun checkStatus(error: Boolean) {
        if (error)
            Toast.makeText(
                this,
                getString(R.string.auth_failed),
                Toast.LENGTH_LONG
            ).show()
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = registerBinding.registerEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            registerBinding.registerEmail.error = "Required."
            valid = false
        } else {
            registerBinding.registerEmail.error = null
        }

        val password = registerBinding.registerPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            registerBinding.registerPassword.error = "Required."
            valid = false
        } else {
            registerBinding.registerPassword.error = null
        }

        val name = registerBinding.registerFullName.text.toString()
        if (TextUtils.isEmpty(password)) {
            registerBinding.registerFullName.error = "Required."
            valid = false
        } else {
            registerBinding.registerFullName.error = null
        }
        return valid
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            imagePickerViewModel.uploadImage(result.data!!.data!!.toString())
                            showLoader(loader, "Uploading Image...")
                            imagePickerViewModel.image.observe(this) { img ->
                                if (img != null) {
//                                    sharedViewModel.observableUserModel.observe(this) { user ->
                                    user.photoUrl = img.toString()
                                    hideLoader(loader)
                                    //}
                                    Picasso.get()
                                        .load(user.photoUrl)
                                        .into(registerBinding.runImage)

                                    registerBinding.chooseImage.setText(R.string.change_image)
                                } // end of if
                            }
                        }
                    }

                    Activity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    private fun setButtonOnClickListeners(
        layout: ActivityRegisterBinding
    ) {
        layout.btnRegister.setOnClickListener() {
            user.displayName = layout.registerFullName.text.toString()
            user.email = layout.registerEmail.text.toString()
            user.registerDate =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
            user.password = layout.registerPassword.text.toString()
            createAccount(user.email, user.password, user.displayName!!)
        }


        layout.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

    }
}





