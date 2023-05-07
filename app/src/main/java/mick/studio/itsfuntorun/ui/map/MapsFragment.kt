package mick.studio.itsfuntorun.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.FragmentMapsBinding
import mick.studio.itsfuntorun.helpers.createLoader
import mick.studio.itsfuntorun.helpers.hideLoader
import mick.studio.itsfuntorun.helpers.showLoader
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.models.SharedViewModel
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import mick.studio.itsfuntorun.ui.camera.ImagePickerFragmentDirections
import mick.studio.itsfuntorun.ui.run.RunViewModel
import mick.studio.itsfuntorun.ui.runlist.RunListViewModel
import java.util.concurrent.TimeUnit


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MapsFragment : Fragment() {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val runListViewModel: RunListViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var runViewModel: RunViewModel

    lateinit var loader: AlertDialog
    private var isActive = false
    private var startTime: Long = 0L
    var startLat: Double = 0.0
    var startLng: Double = 0.0
    var distanceTravelled = 0F
    private var pLines = ArrayList<LatLng>()
    var polylineOptions = PolylineOptions()
    var runModel = RunModel()
    private lateinit var map: GoogleMap
    private var _fragBinding: FragmentMapsBinding? = null
    private var isMapReady: Boolean = false
    private var isStopped: Boolean = false

    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true

        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            if (isActive) {
                if (startTime == 0L) {
                    startTime = mapsViewModel.currentLocation.value!!.elapsedRealtimeMillis
                    startLat = mapsViewModel.currentLocation.value!!.latitude
                    startLng = mapsViewModel.currentLocation.value!!.longitude

                }

                var elapsedTime =
                    mapsViewModel.currentLocation.value!!.elapsedRealtimeMillis - startTime
                fragBinding.runInTime.text = formatToDigitalClock(elapsedTime)
                fragBinding.runSpeed.text =
                    String.format("%.2f", mapsViewModel.currentLocation.value!!.speed).toDouble()
                        .toString()


                isMapReady = true

                distanceTravelled += distanceInMeter(
                    startLat,
                    startLng,
                    mapsViewModel.currentLocation.value!!.latitude,
                    mapsViewModel.currentLocation.value!!.longitude
                )
                //distanceTravelled += distanceInMeter(startLat, startLng, 52.259320, -7.110070)
                fragBinding.runInKms.text =
                    String.format("%.2f", distanceTravelled).toFloat().toString()
                startLat = mapsViewModel.currentLocation.value!!.latitude
                startLng = mapsViewModel.currentLocation.value!!.longitude

                if(isStopped){
                        runModel = RunModel(
                            lat = mapsViewModel.currentLocation.value!!.latitude,
                            lng = mapsViewModel.currentLocation.value!!.longitude,
                            runTime = fragBinding.runInTime.text.toString(),
                            speed = fragBinding.runSpeed.text.toString().toDouble(),
                            distance = fragBinding.runInKms.text.toString().toDouble(),
                            finishTime = fragBinding.runInTime.text.toString(),
                            amountOfCals = fragBinding.runInKms.text.toString().toDouble() * 0.06
                        )
                    sharedViewModel.setRunModel(runModel)
                }

            }

            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )
            val marker=MarkerOptions().position(loc).title("Marker in bbw")
            //set custom icon
            marker.icon(BitmapFromVector(requireContext(), R.drawable.baseline_run_circle_24))
            //add marker
            mapsViewModel.map.addMarker(marker)

            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true

            runListViewModel.observableRunsList.observe(
                viewLifecycleOwner,
                Observer { runs ->
                    runs?.let {
                        render(runs as ArrayList<RunModel>)
                        hideLoader(loader)
                    }
                }
            )



        }
    }

    private fun BitmapFromVector(context:Context,vectorResId:Int): BitmapDescriptor? {
        //drawable generator
        var vectorDrawable: Drawable
        vectorDrawable= ContextCompat.getDrawable(context,vectorResId)!!
        vectorDrawable.setBounds(0,0,vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)
        //bitmap genarator
        var bitmap:Bitmap
        bitmap= Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        //canvas genaret
        var canvas:Canvas
        //pass bitmap in canvas constructor
        canvas= Canvas(bitmap)
        //pass canvas in drawable
        vectorDrawable.draw(canvas)
        //return BitmapDescriptorFactory
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loader = createLoader(requireActivity())

        _fragBinding = FragmentMapsBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.record_a_run)
        runViewModel = ViewModelProvider(this).get(mick.studio.itsfuntorun.ui.run.RunViewModel::class.java)

        fragBinding.stopButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    isStopped = true
                    // The switch is checked.
                } else {
                    isStopped = false
                    // The switch isn't checked.
                }
            }
        val fab: FloatingActionButton = fragBinding.fab

        fab.setOnClickListener {
            if (isActive) {
                isActive = false
                fab.setImageResource(R.drawable.baseline_pause_circle_24)
                fragBinding.stopButton.visibility = View.VISIBLE
            } else {
                isActive = true
                fab.setImageResource(R.drawable.baseline_play_circle_filled_24)
                fragBinding.stopButton.visibility = View.INVISIBLE
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun render(runList: ArrayList<RunModel>) {
        if (!runList.isEmpty()) {
            mapsViewModel.map.clear()
            runList.forEach {
                mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.lat!!, it.lng!!))
                        .title("${it.distance}kms ${it.email}")
                        .snippet(it.email)
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                        )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader, "Downloading Running Info")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser != null) {
                runListViewModel.liveFirebaseUser.value = firebaseUser
                runListViewModel.load()
            }
        }
    }

    fun formatToDigitalClock(miliSeconds: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(miliSeconds).toInt() % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds).toInt() % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds).toInt() % 60
        return when {
            hours > 0 -> String.format("time: %d:%02d:%02d", hours, minutes, seconds)
            minutes > 0 -> String.format("%02d:%02d", minutes, seconds)
            seconds > 0 -> String.format("00:%02d", seconds)
            else -> {
                "00:00"
            }
        }
    }

    private fun distanceInMeter(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ): Float {
        var results = FloatArray(1)
        Location.distanceBetween(startLat, startLon, endLat, endLon, results)
        return results[0]
    }

    private fun drawRoute(lat_lng: ArrayList<LatLng>) {
        polylineOptions.points += lat_lng
        map.addPolyline(polylineOptions)

    }

}
