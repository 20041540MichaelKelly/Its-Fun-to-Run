package mick.studio.itsfuntorun.ui.run

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.FragmentRunBinding
import mick.studio.itsfuntorun.main.MainApp
import mick.studio.itsfuntorun.models.RunModel

class RunFragment : Fragment() {

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    //private lateinit var binding: ActivityRunBinding
    var run = RunModel()
    val runs = ArrayList<RunModel>()
    lateinit var app : MainApp
    var edit = false
    private var _fragBinding: FragmentRunBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var runViewModel: RunViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        //setHasOptionsMenu(true)
        //navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
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
        runViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })
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
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
       /* totalDonated = app.donationsStore.findAll().sumOf { it.amount }
        fragBinding.progressBar.progress = totalDonated
        fragBinding.totalSoFar.text = getString(R.string.totalSoFar,totalDonated)*/
    }

}