package mick.studio.itsfuntorun.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import mick.studio.itsfuntorun.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_run_image.toString())
    intentLauncher.launch(chooseFile)
}