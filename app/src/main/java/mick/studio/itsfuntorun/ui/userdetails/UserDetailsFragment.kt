package mick.studio.itsfuntorun.ui.userdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.databinding.FragmentUserDetailsBinding
import mick.studio.itsfuntorun.models.friends.FriendsModel
import mick.studio.itsfuntorun.models.users.UserModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import mick.studio.itsfuntorun.ui.userlist.UserListFragmentDirections
import mick.studio.itsfuntorun.ui.userlist.UserListViewModel

class UserDetailsFragment : Fragment() {
    private var _fragBinding: FragmentUserDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!

    private val args by navArgs<UserDetailsFragmentArgs>()
    private lateinit var userDetailsViewModel: UserDetailsViewModel
    var user = UserModel()
    lateinit var userListViewModel: UserListViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        Toast.makeText(context, "Run ID Selected : ${args.user.uid}", Toast.LENGTH_LONG).show()
        user = args.user
        userListViewModel = ViewModelProvider(this).get(UserListViewModel::class.java)

        fragBinding.userEmailTV.setText("Email : ${args.user.email}")
        fragBinding.userNameTV.setText("Name : ${args.user.name.toString()}")
        fragBinding.userMemberSinceTV.setText("Joined : ${args.user.registerDate.toString()}")
        if(args.user.image != ""){
            Picasso.get()
                .load(args.user.image)
                .into(fragBinding.profileImage)
        }

        setButtonOnClickListeners(fragBinding)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userDetailsViewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)
    }

    private fun setButtonOnClickListeners(
        layout: FragmentUserDetailsBinding
    ) {
        layout.addFriendButton.setOnClickListener() {
                userDetailsViewModel.addFriend( FriendsModel(uid = loggedInViewModel.liveFirebaseUser.value!!.uid, fid = user.uid), loggedInViewModel.liveFirebaseUser)

        }
            //val runid = updateRunSession.runid
//            updateRunSession.runTime = layout.editRunTime.text.toString()
//            updateRunSession.distance = layout.editRunDistance.text.toString().toDouble()
//            updateRunSession.amountOfCals = layout.editCalories.text.toString().toDouble()
//
//            if (runid != null) {
//                runDetailViewModel.updateUser(
//                    loggedInViewModel.liveFirebaseUser.value!!.uid,
//                    runid, updateRunSession
//                )
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

