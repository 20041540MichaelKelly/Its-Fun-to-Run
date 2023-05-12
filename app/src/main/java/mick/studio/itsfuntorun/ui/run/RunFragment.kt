package mick.studio.itsfuntorun.ui.run

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.BottomNavBarBinding
//import mick.studio.itsfuntorun.ui.map.MapsActivity
import mick.studio.itsfuntorun.databinding.FragmentRunBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.models.SharedViewModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import mick.studio.itsfuntorun.ui.camera.ImagePickerFragmentDirections
import mick.studio.itsfuntorun.ui.camera.ImagePickerViewModel
import mick.studio.itsfuntorun.ui.map.MapsViewModel
import timber.log.Timber.i
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RunFragment : Fragment() {
    var runModel = RunModel()
    private var _fragBinding: FragmentRunBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var runViewModel: RunViewModel
    private lateinit var loggedInViewModel: LoggedInViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var loader: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentRunBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.record_a_run)
        setupMenu()
        loader = createLoader(requireActivity())

        runViewModel = ViewModelProvider(this).get(RunViewModel::class.java)
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)

        runViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }
        })

        //if(args.run != null) {
        sharedViewModel.observableRunModel.observe(viewLifecycleOwner, Observer { run ->
            runModel = updateRunModel(run)
            fragBinding.runKms.setText(run!!.distance.toString())
            fragBinding.runTime.setText(run.finishTime)
            fragBinding.runCalories.setText(run.amountOfCals.toString())
            if (run.image != "") {
                Picasso.get()
                    .load(run.image)
                    .into(fragBinding.runImage)
            }
        })

        setButtonOnClickListeners(fragBinding)
        return root
    }

    private fun setButtonOnClickListeners(
        layout: FragmentRunBinding
    ) {
        layout.btnAdd.setOnClickListener() {
            runModel.distance = layout.runKms.text.toString().toDouble()
            runModel.finishTime = layout.runTime.text.toString()
            runModel.runTime =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
            runModel.amountOfCals = layout.runCalories.text.toString().toDouble()
            if (runModel.finishTime!!.isNotEmpty()) {
                runViewModel.addRun(
                    loggedInViewModel.liveFirebaseUser,
                    updateRunModel(runModel)
                )
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

    }

    private fun updateRunModel(run: RunModel): RunModel {
        return RunModel(
            lat = run.lat,
            lng = run.lng,
            runTime = run.runTime,
            speed = run.speed,
            distance = run.distance,
            finishTime = run.finishTime,
            amountOfCals = run.amountOfCals,
            image = run.image,
            zoom = 15f,
            email = run.email //Need to update with logged in observer to get the email
        )
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
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    findNavController().navigate(R.id.runListFragment)
                }
            }
            false -> Toast.makeText(context, getString(R.string.runError), Toast.LENGTH_LONG).show()
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