package mick.studio.itsfuntorun.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.activities.main.MainApp

class RunListActivity : AppCompatActivity() {
    lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_list)

        app = application as MainApp
    }
}