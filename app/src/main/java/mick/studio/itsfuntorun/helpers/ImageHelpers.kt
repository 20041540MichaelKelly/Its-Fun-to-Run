package mick.studio.itsfuntorun.helpers

import android.content.Intent
import android.graphics.Color
import android.service.autofill.Transformation
import androidx.activity.result.ActivityResultLauncher
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import mick.studio.itsfuntorun.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_run_image.toString())
    intentLauncher.launch(chooseFile)
}

fun customTransformation() : com.squareup.picasso.Transformation =
    RoundedTransformationBuilder()
        .borderColor(Color.BLUE)
        .borderWidthDp(2F)
        .cornerRadiusDp(35F)
        .oval(false)
        .build()
