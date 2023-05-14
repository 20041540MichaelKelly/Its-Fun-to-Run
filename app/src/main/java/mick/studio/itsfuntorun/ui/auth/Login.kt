package mick.studio.itsfuntorun.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.ActivityLoginBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.hideLoader
import mick.studio.itsfuntorun.helpers.showLoader
import mick.studio.itsfuntorun.ui.auth.LoginRegisterViewModel
import mick.studio.itsfuntorun.ui.auth.Register
import mick.studio.itsfuntorun.ui.home.Home
import timber.log.Timber
class Login : AppCompatActivity() {

    private lateinit var loginRegisterViewModel : LoginRegisterViewModel
    private lateinit var loginBinding : ActivityLoginBinding
    lateinit var loader : AlertDialog
    private lateinit var startForResult : ActivityResultLauncher<Intent>


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loader = createLoader(this)

        loginBinding.emailSignInButton.setOnClickListener {
            signIn(loginBinding.fieldEmail.text.toString(),
                loginBinding.fieldPassword.text.toString())
        }
        loginBinding.emailCreateAccountButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            // start your next activity
            startActivity(intent)
//            createAccount(loginBinding.fieldEmail.text.toString(),
//                loginBinding.fieldPassword.text.toString())
        }

        loginBinding.googleSignInButton.setOnClickListener { googleSignIn() }


        loginBinding.googleSignInButton.setSize(SignInButton.SIZE_WIDE)
        loginBinding.googleSignInButton.setColorScheme(0)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        loginRegisterViewModel.liveFirebaseUser.observe(this, Observer
        { firebaseUser -> if (firebaseUser != null)
            hideLoader(loader)
            startActivity(Intent(this, Home::class.java)) })

        loginRegisterViewModel.firebaseAuthManager.errorStatus.observe(this, Observer
        { status -> checkStatus(status) })

        setupGoogleSignInCallback()
    }

    //Required to exit app from Login Screen - must investigate this further
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"Click again to Close App...",Toast.LENGTH_LONG).show()
        finish()
    }

    private fun signIn(email: String, password: String) {
        Timber.d("signIn:$email")
        if (!validateForm()) { return }

        showLoader(loader,"Signing in...")
        loginRegisterViewModel.login(email,password)
        hideLoader(loader)
    }

    private fun checkStatus(error:Boolean) {
        if (error)
            Toast.makeText(this,
                getString(R.string.auth_failed),
                Toast.LENGTH_LONG).show()
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = loginBinding.fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            loginBinding.fieldEmail.error = "Required."
            valid = false
        } else {
            loginBinding.fieldEmail.error = null
        }

        val password = loginBinding.fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            loginBinding.fieldPassword.error = "Required."
            valid = false
        } else {
            loginBinding.fieldPassword.error = null
        }
        return valid
    }

    /**
     * Google signin feature
     */

    private fun googleSignIn() {
        val signInIntent = loginRegisterViewModel.firebaseAuthManager
            .googleSignInClient.value!!.signInIntent

        startForResult.launch(signInIntent)
    }

    private fun setupGoogleSignInCallback() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            loginRegisterViewModel.authWithGoogle(account!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed
                            Timber.i( "Google sign in failed $e")
                            Snackbar.make(loginBinding.root, "Authentication Failed.",
                                Snackbar.LENGTH_SHORT).show()
                        }
                        Timber.i("DonationX Google Result $result.data")
                    }
                    RESULT_CANCELED -> {

                    } else -> { }
                }
            }
    }
}
