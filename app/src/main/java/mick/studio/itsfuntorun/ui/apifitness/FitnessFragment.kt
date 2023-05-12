package mick.studio.itsfuntorun.ui.apifitness

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.adapter.FitnessAdapter
import mick.studio.itsfuntorun.adapter.FitnessListener
import mick.studio.itsfuntorun.databinding.FragmentFitnessBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.hideLoader
import mick.studio.itsfuntorun.helpers.showLoader
import mick.studio.itsfuntorun.models.fitness.FitnessModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import mick.studio.itsfuntorun.ui.run.RunViewModel
import mick.studio.itsfuntorun.ui.runlist.RunListViewModel

class FitnessFragment : Fragment(), FitnessListener {
    private var _fragBinding: FragmentFitnessBinding? = null
    private val fragBinding get() = _fragBinding!!
    var fitnessItem = FitnessModel()
    lateinit var loader: AlertDialog

    private lateinit var fitnessViewModel: FitnessViewModel
    private lateinit var runListViewModel: RunListViewModel
    lateinit var runViewModel: RunViewModel

    private val loggedInViewModel: LoggedInViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentFitnessBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        loader = createLoader(requireActivity())
        fitnessViewModel = ViewModelProvider(this).get(FitnessViewModel::class.java)
        showLoader(loader, "Downloading Fitness Instructions")
        fitnessViewModel.observableApiFoodItemsList.observe(
            viewLifecycleOwner,
            Observer { excersise ->
                excersise?.let {
                    render(excersise)
                    hideLoader(loader)
                }
            })
//        myFoodListViewModel = ViewModelProvider(this).get(MyFoodListViewModel::class.java)
//        runViewModel = ViewModelProvider(this).get(MyFoodDiaryViewModel::class.java)
//
//
        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
//            val action = MyFoodListFragmentDirections.actionMyFoodListFragmentToMyFoodDiaryFragment()
//            findNavController().navigate(action)
        }
        setButtonListener(fragBinding)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_run, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    private fun render(excersises: List<FitnessModel>) {
        fragBinding.recyclerView.adapter = FitnessAdapter(excersises,this)

        if (excersises.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.fitnessItemsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.fitnessItemsNotFound.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        fitnessViewModel.load()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

//    override fun onFoodItemClick(excersise: FitnessModel) {
//        //addToFirebaseDB(excersise)
//    }

//    private fun addToFirebaseDB(fitnessModel: FitnessModel) {
//        runViewModel.addFoodItem(
//            loggedInViewModel.liveFirebaseUser,
//            FoodModel(
//                title = foodItem.title,
//                description = foodItem.description,
//                amountOfCals = foodItem.amountOfCals,
//                dateLogged = LocalDateTime.now()
//                    .format(DateTimeFormatter.ofPattern("M/d/y H:m:ss")),
//                timeForFood = System.currentTimeMillis().toString(),
//                image = foodItem.image,
//                email = loggedInViewModel.liveFirebaseUser.value?.email!!,
//                lat = foodItem.lat,
//                lng = foodItem.lng,
//                zoom = 15f
//            )
//        )
//        Toast.makeText(context,"Food Item added",Toast.LENGTH_LONG).show()
//
//    }



    fun setButtonListener(
        layout: FragmentFitnessBinding
    ){



        layout.filterBtn.setOnClickListener() {
            val muscle = layout.muscle.text.toString()

            fitnessViewModel.filterApi(muscle)
            Toast.makeText(context,"Excersises returnes for : ${muscle}",Toast.LENGTH_LONG).show()
        }

    }

    override fun onFitnessClick(excersise: FitnessModel) {
        TODO("Not yet implemented")
    }
}