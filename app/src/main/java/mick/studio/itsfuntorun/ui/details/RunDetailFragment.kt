package mick.studio.itsfuntorun.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import mick.studio.itsfuntorun.R

class RunDetailFragment : Fragment() {

    private val args by navArgs<RunDetailFragmentArgs>()

    private lateinit var viewModel: RunDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RunDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_run_detail, container, false)

        Toast.makeText(context,"Run ID Selected : ${args.runid}",Toast.LENGTH_LONG).show()

        return view
    }
}