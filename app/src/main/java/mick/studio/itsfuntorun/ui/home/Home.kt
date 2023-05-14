package mick.studio.itsfuntorun.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.activities.Login
import mick.studio.itsfuntorun.databinding.HomeBinding
import mick.studio.itsfuntorun.databinding.NavHeaderBinding
import mick.studio.itsfuntorun.helpers.checkLocationPermissions
import mick.studio.itsfuntorun.helpers.customTransformation
import mick.studio.itsfuntorun.helpers.isPermissionGranted
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import mick.studio.itsfuntorun.ui.camera.ImagePickerFragmentDirections
import mick.studio.itsfuntorun.ui.map.MapsViewModel
import mick.studio.itsfuntorun.ui.userdetails.UserDetailsViewModel
import mick.studio.itsfuntorun.ui.userlist.UserListViewModel
import timber.log.Timber

class Home : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel : LoggedInViewModel
    private val mapsViewModel : MapsViewModel by viewModels()
    private lateinit var userListViewModel: UserListViewModel
    var run = RunModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        if(checkLocationPermissions(this)) {
            mapsViewModel.updateCurrentLocation()
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //Initialize the bottom navigation view
        //create bottom navigation view object
         //  val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        //}

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.runFragment, R.id.runListFragment, R.id.mapsFragment, R.id.fitnessFragment,R.id.imagePickerFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = homeBinding.navView

        navView.setupWithNavController(navController)
       // bottomNavigationView?.setupWithNavController(navController)
    }
    
    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        userListViewModel = ViewModelProvider(this).get(UserListViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
        })
        userListViewModel.loadAll()

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })

    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        val headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        navHeaderBinding.navHeaderEmail.text = currentUser.email
        navHeaderBinding.navHeaderName.text = currentUser.displayName
        if(currentUser.photoUrl == null){
            userListViewModel.observableUsersList.observe(this, Observer { users ->
                users.forEach{ user ->
                    if(user.uid == currentUser.uid){
                        Picasso.get().load(user.photoUrl)
                            .resize(200, 200)
                            .transform(customTransformation())
                            .centerCrop()
                            .into(navHeaderBinding.imageView)

                        navHeaderBinding.navHeaderName.text = user.displayName
                    }
                }

            })
        }else{
            Picasso.get().load(currentUser.photoUrl)
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(navHeaderBinding.imageView)

            navHeaderBinding.navHeaderName.text = currentUser.displayName
        }



    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        //Launch Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isPermissionGranted(requestCode, grantResults))
            mapsViewModel.updateCurrentLocation()
        else {
            // permissions denied, so use a default location
            mapsViewModel.currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Timber.i("LOC : %s", mapsViewModel.currentLocation.value)
    }
}
