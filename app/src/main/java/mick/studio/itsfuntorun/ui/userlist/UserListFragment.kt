package mick.studio.itsfuntorun.ui.userlist

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.adapter.RunListAdapter
import mick.studio.itsfuntorun.adapter.UserListAdapter
import mick.studio.itsfuntorun.adapter.UserListener
import mick.studio.itsfuntorun.databinding.FragmentRunListBinding
import mick.studio.itsfuntorun.databinding.FragmentUserListBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.hideLoader
import mick.studio.itsfuntorun.helpers.showLoader
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.models.users.UserModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import mick.studio.itsfuntorun.ui.runlist.RunListFragmentDirections
import mick.studio.itsfuntorun.ui.runlist.RunListViewModel
import timber.log.Timber

class UserListFragment : Fragment(), UserListener {

    private var _fragBinding: FragmentUserListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var userListViewModel: UserListViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

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
        _fragBinding = FragmentUserListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        userListViewModel = ViewModelProvider(this).get(UserListViewModel::class.java)
        showLoader(loader, "Loading Users")

        userListViewModel.observableUsersList.observe(viewLifecycleOwner, Observer {
                users ->
            users?.let {
                Timber.i("Users Load Success : $users")

                render(users as ArrayList<UserModel>)
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

    private fun render(userList: List<UserModel>) {
        fragBinding.recyclerView.adapter = UserListAdapter(userList, this)
        if (userList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.usersNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.usersNotFound.visibility = View.GONE
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
                userListViewModel.liveFirebaseUser.value = firebaseUser
                userListViewModel.load()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onUserClick(user: UserModel) {
        TODO("Not yet implemented")
    }

}