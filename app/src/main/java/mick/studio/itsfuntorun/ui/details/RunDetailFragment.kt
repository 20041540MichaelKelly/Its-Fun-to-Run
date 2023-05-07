package mick.studio.itsfuntorun.ui.details

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.FragmentRunDetailBinding
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import mick.studio.itsfuntorun.ui.runlist.RunListViewModel

class RunDetailFragment : Fragment() {
    private var _fragBinding: FragmentRunDetailBinding? = null
    private val fragBinding get() = _fragBinding!!

    private val args by navArgs<RunDetailFragmentArgs>()
    private lateinit var runDetailViewModel: RunDetailViewModel
    var runModel = RunModel()
    lateinit var runListViewModel: RunListViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private lateinit var updateRunSession: RunModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentRunDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        Toast.makeText(context, "Run ID Selected : ${args.runid}", Toast.LENGTH_LONG).show()

        runListViewModel = ViewModelProvider(this).get(RunListViewModel::class.java)

        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                runListViewModel.liveFirebaseUser.value = firebaseUser
                runListViewModel.load()
                // myFoodListViewModel.getCordinates()
                runListViewModel.observableRunsList.observe(
                    viewLifecycleOwner, Observer { runs ->
                        runs?.let {
                            runs.forEach { run ->
                                if (runModel.runid == args.runid) {
                                    getTheRun(run)
                                }
                            }
                        }
                    })
            }
        })
        setButtonOnClickListeners(fragBinding)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        runDetailViewModel = ViewModelProvider(this).get(RunDetailViewModel::class.java)
    }

    private fun getTheRun(run: RunModel) {
        updateRunSession = run

        fragBinding.editRunTime.setText(run.runTime)
        fragBinding.editRunDistance.setText(run.distance.toString())
        fragBinding.editCalories.setText(run.amountOfCals.toString())
        //fragBinding.image.setImageURI(Uri.parse(run.image))
    }

    private fun setButtonOnClickListeners(
        layout: FragmentRunDetailBinding
    ) {
        layout.editRunButton.setOnClickListener() {
            val runid = updateRunSession.runid
            updateRunSession.runTime = layout.editRunTime.text.toString()
            updateRunSession.distance = layout.editRunDistance.text.toString().toDouble()
            updateRunSession.amountOfCals = layout.editCalories.text.toString().toDouble()

            if (runid != null) {
                runDetailViewModel.updateRun(
                    loggedInViewModel.liveFirebaseUser.value!!.uid,
                    runid, updateRunSession
                )
                Toast.makeText(context, "Run Updated", Toast.LENGTH_LONG).show()

            }
        }

//        layout.deleteRunButton.setOnClickListener(){
//            if(updateRunSession.runid != "") {
//                runListViewModel.deleteRun(
//                    loggedInViewModel.liveFirebaseUser.value!!.uid,
//                    updateRunSession
//                )
//                Snackbar.make(it, R.string.deleted_run_wait, Snackbar.LENGTH_LONG)
//                    .show()
//                Handler().postDelayed({
//                    val action =
//                        IndividualFoodItemFragmentDirections.actionIndividualFoodItemFragmentToMyFoodListFragment(
//                            usedForUpdateFoodItem.timeForFood.toLong()
//                        )
//                    findNavController().navigate(action)
//                }, 1500)
//            }
//        }
//    }
    }
}