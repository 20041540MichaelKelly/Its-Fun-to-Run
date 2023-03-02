package mick.studio.itsfuntorun.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.main.MainApp
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.databinding.ActivityRunBinding
import mick.studio.itsfuntorun.helpers.showImagePicker
import mick.studio.itsfuntorun.models.Location
import timber.log.Timber
import timber.log.Timber.i

class RunActivity : AppCompatActivity() {

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityRunBinding
    var run = RunModel()
    val runs = ArrayList<RunModel>()
    lateinit var app : MainApp
    var edit = false
    var location = Location(52.245696, -7.139102, 15f)

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

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.btnAdd.setOnClickListener() {
            addButtonClicked()
        }

        binding.runLocation.setOnClickListener {
            val launcherIntent = Intent(this, MapsActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
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
            Picasso.get()
                .load(run.image)
                .into(binding.runImage)
            if (run.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_run_image)
            }
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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            run.image = result.data!!.data!!
                            Picasso.get()
                                .load(run.image)
                                .into(binding.runImage)
                            binding.chooseImage.setText(R.string.change_run_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            i("Location == $location")
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

}