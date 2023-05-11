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
import com.google.android.material.bottomnavigation.BottomNavigationView
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.FragmentPieChartBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.hideLoader
import mick.studio.itsfuntorun.helpers.showLoader
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel

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
        loader = createLoader(requireActivity())
        pieChart = fragBinding.runPieChart

        showLoader(loader, "fetching user data...")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                analysisViewModel.liveFirebaseUser.value = firebaseUser
                analysisViewModel.load()
                analysisViewModel.observableRunsList.observe(
                    viewLifecycleOwner, Observer { runs ->
                        runs?.let {
                            runs.forEach { run ->
                                addPointsToList(run.amountOfCals.toString().toFloat(), run.runTime.toString())
                                hideLoader(loader)
                                drawPieChart(pieChartList)
                            }
                        }

                    }
                )
            }
        })

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

}