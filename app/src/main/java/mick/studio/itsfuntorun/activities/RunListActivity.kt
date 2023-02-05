package mick.studio.itsfuntorun.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import mick.studio.itsfuntorun.adapter.RunListAdapter
import mick.studio.itsfuntorun.main.MainApp
import mick.studio.itsfuntorun.databinding.ActivityRunListBinding

class RunListActivity : AppCompatActivity() {
    lateinit var app: MainApp
    private lateinit var binding: ActivityRunListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        val layoutManager= LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = RunListAdapter(app.runs)
    }
}