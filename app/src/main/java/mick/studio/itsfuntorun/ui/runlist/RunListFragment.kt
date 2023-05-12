package mick.studio.itsfuntorun.ui.runlist

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.adapter.RunListAdapter
import mick.studio.itsfuntorun.adapter.RunListener
import mick.studio.itsfuntorun.databinding.FragmentRunListBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.hideLoader
import mick.studio.itsfuntorun.helpers.showLoader
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel

class RunListFragment : Fragment(), RunListener {

    private var _fragBinding: FragmentRunListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var runListViewModel: RunListViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    var readOnly = MutableLiveData(false)

    lateinit var loader: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentRunListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        runListViewModel = ViewModelProvider(this).get(RunListViewModel::class.java)
        showLoader(loader, "Downloading Runs")

        runListViewModel.observableRunsList.observe(viewLifecycleOwner, Observer {
                runs ->
            runs?.let {
                render(runs as ArrayList<RunModel>)
                hideLoader(loader)
                //checkSwipeRefresh()
            }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
//            val action = RunListFragmentDirections.actionRunListFragmentToRunFragment(null)
//            findNavController().navigate(action)
        }
//        setSwipeRefresh()
//
//        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                showLoader(loader,"Deleting Donation")
//                val adapter = fragBinding.recyclerView.adapter as DonationAdapter
//                adapter.removeAt(viewHolder.adapterPosition)
//                reportViewModel.delete(reportViewModel.liveFirebaseUser.value?.uid!!,
//                    (viewHolder.itemView.tag as DonationModel).uid!!)
//
//                hideLoader(loader)
//            }
//        }
//        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
//        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)
//
//        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                onDonationClick(viewHolder.itemView.tag as DonationModel)
//            }
//        }
//        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
//        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

        return root
    }


    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_run, menu)

                val item = menu.findItem(R.id.toggleDonations) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleDonations: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleDonations.isChecked = false

                toggleDonations.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) runListViewModel.loadAllFriends()
                    else runListViewModel.load()
                }
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

    private fun render(runList: List<RunModel>) {
        fragBinding.recyclerView.adapter = RunListAdapter(runList, this)
        if (runList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.runsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.runsNotFound.visibility = View.GONE
        }
    }

//private fun setSwipeRefresh() {
//    fragBinding.swiperefresh.setOnRefreshListener {
//        fragBinding.swiperefresh.isRefreshing = true
//        showLoader(loader,"Downloading Donations")
//        reportViewModel.load()
//    }
//}

//private fun checkSwipeRefresh() {
//    if (fragBinding.swiperefresh.isRefreshing)
//        fragBinding.swiperefresh.isRefreshing = false
//}

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                runListViewModel.liveFirebaseUser.value = firebaseUser
                runListViewModel.load()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onRunClick(run: RunModel) {
        if (loggedInViewModel.liveFirebaseUser.value!!.uid == run.uid) {

            val action =
                run.runid?.let {
                    RunListFragmentDirections.actionRunListFragmentToRunDetailFragment(
                        it
                    )
                }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
    }


}