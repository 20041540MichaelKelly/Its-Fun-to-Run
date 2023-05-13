package mick.studio.itsfuntorun.ui.analysis

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import mick.studio.itsfuntorun.databinding.FragmentPieChartBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.hideLoader
import mick.studio.itsfuntorun.helpers.showLoader
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import mick.studio.itsfuntorun.ui.runlist.RunListViewModel
import mick.studio.itsfuntorun.ui.userdetails.UserDetailsViewModel

class PieChartFragment : Fragment() {
    var run = RunModel()
    private var _fragBinding: FragmentPieChartBinding? = null
    private lateinit var analysisViewModel: AnalysisViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var pieChart: PieChart
    lateinit var loader: AlertDialog
    val pieChartList:ArrayList<PieEntry> = ArrayList()
    private val userDetailsViewModel: UserDetailsViewModel by activityViewModels()
    private lateinit var runListViewModel: RunListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragBinding = FragmentPieChartBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        analysisViewModel = ViewModelProvider(this).get(AnalysisViewModel::class.java)
        runListViewModel = ViewModelProvider(this).get(RunListViewModel::class.java)
        loader = createLoader(requireActivity())
        pieChart = fragBinding.runPieChart

        showLoader(loader, "fetching user data...")
        getFriendsRuns()

        return root
    }

    private fun addPointsToList(fCaloriesBurned: Float, fDateOfCaloriesBurned: String) {
        pieChartList.add(PieEntry(fCaloriesBurned, fDateOfCaloriesBurned))
    }

    private fun drawPieChart(list: ArrayList<PieEntry>) {
        pieChart.setEntryLabelColor(Color.BLACK);

        val pieDataSet = PieDataSet(list, "List")

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS, 255)
        pieDataSet.valueTextColor= Color.BLACK
        pieDataSet.valueTextSize = 15f
        val pieData= PieData(pieDataSet)
        pieChart.data=pieData
        pieChart.description.text="Calories Pie Chart"
        pieChart.animateY(2500)
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
                        if (friend.pid == run.uid)
                            addPointsToList(
                                run.amountOfCals.toString().toFloat(),
                                run.displayName.toString()
                            )
                        hideLoader(loader)
                        drawPieChart(pieChartList)
                    }

                })
            }
        })
    }

}