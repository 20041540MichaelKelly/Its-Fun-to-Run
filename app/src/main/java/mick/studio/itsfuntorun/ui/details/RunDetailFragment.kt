package mick.studio.itsfuntorun.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mick.studio.itsfuntorun.R

class RunDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RunDetailFragment()
    }

    private lateinit var viewModel: RunDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_run_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RunDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}