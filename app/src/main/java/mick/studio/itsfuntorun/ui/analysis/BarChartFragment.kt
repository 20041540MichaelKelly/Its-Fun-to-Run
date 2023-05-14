package mick.studio.itsfuntorun.ui.analysis

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.FragmentBarChartBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.hideLoader
import mick.studio.itsfuntorun.helpers.showLoader
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import mick.studio.itsfuntorun.ui.runlist.RunListViewModel
import mick.studio.itsfuntorun.ui.userdetails.UserDetailsViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [BarChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarChartFragment : Fragment() {
    var run = RunModel()
    private var _fragBinding: FragmentBarChartBinding? = null
    private lateinit var analysisViewModel: AnalysisViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private val userDetailsViewModel: UserDetailsViewModel by activityViewModels()
    private lateinit var runListViewModel: RunListViewModel


    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var barChart: BarChart
    lateinit var loader: AlertDialog
    val barList: ArrayList<BarEntry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentBarChartBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        analysisViewModel = ViewModelProvider(this).get(AnalysisViewModel::class.java)
        loader = createLoader(requireActivity())
        runListViewModel = ViewModelProvider(this).get(RunListViewModel::class.java)
        barChart = fragBinding.runBarChart
        showLoader(loader, "fetching user data...")

       getFriendsRuns()


        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            findNavController().navigate(R.id.pieChartFragment)
        }

        return root
    }

    private fun addPointsToList(fDistance: Float, fSixKm: Float) {
        barList.add(BarEntry(fDistance, fSixKm))
    }

    private fun drawBarChart(list: ArrayList<BarEntry>) {
        val barDataSet = BarDataSet(list, "List")

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS, 255)
        barDataSet.valueTextColor = Color.BLACK
        val barData = BarData(barDataSet)
        barChart.setFitBars(true)
        barChart.data = barData
        barChart.description.text = "Bar Chart"
        barChart.animateY(1500)
    }

    private fun getFriendsRuns() {
        showLoader(loader, "Loading Friends")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                userDetailsViewModel.liveFirebaseUser.value = firebaseUser
                userDetailsViewModel.findAllMyFriends()
                runListViewModel.loadAllFriendsRuns()
            }
        })

        userDetailsViewModel.observableFriends.observe(viewLifecycleOwner, Observer { friends ->
            hideLoader(loader)
            if(friends.size == 0)  {
                Snackbar.make(fragBinding.root, "No friends yet...",
                    Snackbar.LENGTH_SHORT).show()}
            friends.forEach { friend ->

                runListViewModel.observableRunsList.observe(viewLifecycleOwner, Observer { runs ->

                    runs.forEach { run ->

                        showLoader(loader, "Loading Friends")
                        if (friend.pid == run.uid || run.uid == loggedInViewModel.liveFirebaseUser.value!!.uid)
                            addPointsToList(
                                "5.0".toFloat(),
                                run.distance.toString().toFloat(),

                            )
                        drawBarChart(barList)
                        hideLoader(loader)
                    }

                })
            }
        })

    }
}