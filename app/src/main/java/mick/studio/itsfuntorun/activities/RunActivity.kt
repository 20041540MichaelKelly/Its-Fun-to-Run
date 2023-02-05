package mick.studio.itsfuntorun.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import mick.studio.itsfuntorun.main.MainApp
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.databinding.ActivityRunBinding
import timber.log.Timber
import timber.log.Timber.i

class RunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunBinding
    var run = RunModel()
    val runs = ArrayList<RunModel>()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        i("Run Activity started...")

        app = application as MainApp

        binding.btnAdd.setOnClickListener() {
            run.runInKms = binding.runKms.text.toString()
            run.runInTime = binding.runTime.text.toString()

            if (run.runInKms.isNotEmpty() && run.runInTime.isNotEmpty()) {
                app.runs.add(run.copy())
                i("add Button Pressed: ${run.runInKms}")
                for(i in runs.indices){
                    i("run[$i]:${this.runs[i]} ${this.runs.size}")
                }
            }
            else {
                Snackbar
                    .make(it,"Please Enter a run", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

    }
}