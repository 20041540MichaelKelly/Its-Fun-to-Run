package mick.studio.itsfuntorun.ui.run

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.ui.map.MapsActivity
import mick.studio.itsfuntorun.databinding.FragmentRunBinding
import mick.studio.itsfuntorun.helpers.showImagePicker
import mick.studio.itsfuntorun.models.Location
import mick.studio.itsfuntorun.models.RunModel
import timber.log.Timber.i

class RunFragment : Fragment() {

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var run = RunModel()
    //lateinit var app : MainApp
    private var _fragBinding: FragmentRunBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var runViewModel: RunViewModel
    var edit = false
    var location = Location(52.245696, -7.139102, 15f)

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
            run.runInKms = fragBinding.runKms.text.toString()
            run.runInTime = fragBinding.runTime.text.toString()

            if (run.runInKms.isNotEmpty() && run.runInTime.isNotEmpty()) {
            if (edit) runViewModel.addRun(run.copy()) else runViewModel.addRun(run.copy())
            i("add Button Pressed: ${run.runInKms}")

            
         //   finish()
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

        layout.runLocation.setOnClickListener {
            val launcherIntent = Intent(this.requireActivity(), MapsActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

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
                            run.image = result.data!!.data!!
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
                            location = result.data!!.extras?.getParcelable("location")!!
                            i("Location == $location")
                        } // end of if
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