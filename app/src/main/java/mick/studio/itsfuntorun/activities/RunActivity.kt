package mick.studio.itsfuntorun.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import mick.studio.itsfuntorun.activities.models.RunModel
import mick.studio.itsfuntorun.databinding.ActivityRunBinding
import timber.log.Timber
import timber.log.Timber.i

class RunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRunBinding
    var run = RunModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        i("Run Activity started...")

        binding.btnAdd.setOnClickListener() {
            run.runInKms = binding.runKms.text.toString()
            if (run.runInKms.isNotEmpty()) {
                i("add Button Pressed: ${run.runInKms}")
            }
            else {
                Snackbar
                    .make(it,"Please Enter a run", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

    }
}