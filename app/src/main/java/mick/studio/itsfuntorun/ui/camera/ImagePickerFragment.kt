package mick.studio.itsfuntorun.ui.camera

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.SupportMapFragment
import com.squareup.picasso.Picasso
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.FragmentImagePickerBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.hideLoader
import mick.studio.itsfuntorun.helpers.showImagePicker
import mick.studio.itsfuntorun.helpers.showLoader
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.ui.run.RunViewModel
import mick.studio.itsfuntorun.ui.runlist.RunListFragmentDirections
import timber.log.Timber

class ImagePickerFragment : Fragment() {

    private lateinit var imagePickerViewModel: ImagePickerViewModel
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    var run = RunModel()
    private var _fragBinding: FragmentImagePickerBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    lateinit var loader: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentImagePickerBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        loader = createLoader(requireActivity())

        imagePickerViewModel = ViewModelProvider(this).get(ImagePickerViewModel::class.java)

        registerImagePickerCallback()
        setButtonOnClickListeners(fragBinding)

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun setButtonOnClickListeners(
        layout: FragmentImagePickerBinding
    ) {
        layout.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")

                            imagePickerViewModel.uploadImage(result.data!!.data!!.toString())
//                            imagePickerViewModel.image = result.data!!.data!!.toString()
                            showLoader(loader, "Uploading Image...")
                            imagePickerViewModel.image.observe(viewLifecycleOwner) { img ->
                                if (img != null) {
                                    hideLoader(loader)
                                    run.image = img.toString()
                                    Picasso.get()
                                        .load(img)
                                        .into(fragBinding.runImage)
                                    val action = ImagePickerFragmentDirections.actionImagePickerFragmentToRunFragment(run)
                                    findNavController().navigate(action)
                                }
                            }

                        } // end of if
                    }
                    Activity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

}