package mick.studio.itsfuntorun.ui.run

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.location.Location
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.BottomNavBarBinding
//import mick.studio.itsfuntorun.ui.map.MapsActivity
import mick.studio.itsfuntorun.databinding.FragmentRunBinding
import mick.studio.itsfuntorun.databinding.NavHeaderBinding
import mick.studio.itsfuntorun.helpers.showImagePicker
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import timber.log.Timber.i
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RunFragment : Fragment() {

    private lateinit var navBarBinding: BottomNavBarBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var run = RunModel()
    //lateinit var app : MainApp
    private var _fragBinding: FragmentRunBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var runViewModel: RunViewModel
    private lateinit var loggedInViewModel: LoggedInViewModel

    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentRunBinding.inflate(inflater, container, false )
        val root = fragBinding.root
        activity?.title = getString(R.string.record_a_run)
        setupMenu()
        runViewModel = ViewModelProvider(this).get(RunViewModel::class.java)
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)

        runViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
            status -> status?.let { render(status)}
        })
        setButtonOnClickListeners(fragBinding)
        registerImagePickerCallback()
        registerMapCallback()
        return root
    }

    private fun setButtonOnClickListeners(
        layout: FragmentRunBinding
    ){
        layout.btnAdd.setOnClickListener() {
            run.distance = layout.runKms.text.toString().toDouble()
            run.finishTime = layout.runTime.text.toString()
            run.runTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
           // run.amountOfCals =
            if (run.finishTime!!.isNotEmpty()) {
           // if (edit) {
                runViewModel.addRun(
                    loggedInViewModel.liveFirebaseUser,
                    RunModel(
                        lat = run.lat, lng = run.lng, runTime = run.runTime,speed = run.speed,  distance = run.distance , finishTime = run.finishTime,
                         amountOfCals = run.amountOfCals,image = run.image, zoom = run.zoom, email = loggedInViewModel.liveFirebaseUser.value?.email!!
                    )

                )


                i("add Button Pressed: ${run.distance}")
          //  }

            
            } else {
                Snackbar
                    .make(
                        fragBinding.btnAdd,
                        getString(R.string.enter_a_run_error),
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }
        }

//        layout.runLocation.setOnClickListener {
//            val launcherIntent = Intent(this.requireActivity(), MapsActivity::class.java)
//                .putExtra("location", LatLng(52.4720715,-6.8861582))
//            mapIntentLauncher.launch(launcherIntent)
//        }

        layout.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            run.image = result.data!!.data!!.toString()
                            Picasso.get()
                                .load(run.image)
                                .into(fragBinding.runImage)
                            fragBinding.chooseImage.setText(R.string.change_run_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val runLoc = result.data!!.extras?.getParcelable<RunModel>("location")!!
                        if(runLoc.lat == 0.0) {
                            i("Location == $runLoc")
                        }else {
                            i("Location == $runLoc")
                            run.lat = runLoc.lat
                            run.lng = runLoc.lng
                            run.zoom = runLoc.zoom
                        }
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_run, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.runError),Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
    }

}