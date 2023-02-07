package mick.studio.itsfuntorun.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import mick.studio.itsfuntorun.R
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
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        Timber.plant(Timber.DebugTree())

        i("Run Activity started...")

        app = application as MainApp

        checkHasIntentData(intent)

        binding.btnAdd.setOnClickListener() {
            addButtonClicked()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_run, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

     fun checkHasIntentData(intent: Intent) {
        if (intent.hasExtra("run_edit")) {
            run = intent.extras?.getParcelable("run_edit")!!
            binding.runKms.setText(run.runInKms)
            binding.runTime.setText(run.runInTime)
            binding.btnAdd.text = getString(R.string.save_run)
        }
    }

    private fun addButtonClicked() {
        run.runInKms = binding.runKms.text.toString()
        run.runInTime = binding.runTime.text.toString()

        if (run.runInKms.isNotEmpty() && run.runInTime.isNotEmpty()) {
            if (edit) app.runs.update(run.copy()) else app.runs.create(run.copy())
            app.runs.create(run.copy())
            i("add Button Pressed: ${run.runInKms}")
            for(i in runs.indices){
                i("run[$i]:${this.runs[i]} ${this.runs.size}")
            }
            setResult(RESULT_OK)
            finish()
        }
        else {
            Snackbar
                .make(binding.btnAdd,getString(R.string.enter_a_run_error), Snackbar.LENGTH_LONG)
                .show()
        }
    }
}