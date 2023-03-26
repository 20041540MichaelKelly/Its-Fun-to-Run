package mick.studio.itsfuntorun.ui.runlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
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
import mick.studio.itsfuntorun.models.RunModel

class RunListFragment : Fragment(), RunListener {

    private var _fragBinding: FragmentRunListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var runListViewModel: RunListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentRunListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        runListViewModel = ViewModelProvider(this).get(RunListViewModel::class.java)
        runListViewModel.observableRunsList.observe(viewLifecycleOwner, Observer {
                runs ->
            runs?.let { render(runs) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = RunListFragmentDirections.actionRunListFragmentToRunFragment()
            findNavController().navigate(action)
        }
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
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(runList: List<RunModel>) {
        fragBinding.recyclerView.adapter = RunListAdapter(runList, this)
        if (runList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.donationsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.donationsNotFound.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        runListViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onRunClick(run: RunModel) {
        val action = RunListFragmentDirections.actionRunListFragmentToRunDetailFragment(run.id)
        findNavController().navigate(action)
    }
}