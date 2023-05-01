package mick.studio.itsfuntorun.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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
import mick.studio.itsfuntorun.ui.auth.LoggedInViewModel
import mick.studio.itsfuntorun.ui.runlist.RunListViewModel
import java.util.concurrent.TimeUnit


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MapsFragment : Fragment(),
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val runListViewModel: RunListViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
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

    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
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

//                mapsViewModel.addToPolyLineLatLng(
//                    LatLng(
//                        mapsViewModel.currentLocation.value!!.latitude,
//                        mapsViewModel.currentLocation.value!!.longitude
//                    )
//                )
                    polylineOptions.points += LatLng(
                        mapsViewModel.currentLocation.value!!.latitude,
                        mapsViewModel.currentLocation.value!!.longitude
                    )

                   mapsViewModel.map.addPolyline(polylineOptions)

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
            }

            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loader = createLoader(requireActivity())

        _fragBinding = FragmentMapsBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.record_a_run)
        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            if (isActive) {
                isActive = false
                fab.setImageResource(R.drawable.baseline_cancel_24)
                mapsViewModel.observableLatLngs.observe(viewLifecycleOwner, Observer {
                    if (it != null)
                        for (ltLg in it) {
                            var lineOptions = PolylineOptions()
                                .add(ltLg)
                                .color(Color.RED)
                                .width(8F)
                        }
//
                })
            } else {
                isActive = true
                fab.setImageResource(R.drawable.baseline_play_circle_filled_24)

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
                    MarkerOptions().position(LatLng(it.lat, it.lng))
                        .title("${it.distance}kms ${it.email}")
                        .snippet(it.email)
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
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

    fun drawRoute(lat_lng: LatLng) {
        //polylineOptions.points += lat_lng
        mapsViewModel.map.addPolyline(
            PolylineOptions().add(lat_lng).color(Color.GREEN)
                .width(56F)
        );

    }

    override fun onMarkerDrag(marker: Marker) {
        if (onMarkerClick(marker)) {
            onMarkerDragStart(marker)
        }
    }

    override fun onMarkerDragEnd(marker: Marker) {
        runModel.lat = marker.position.latitude
        runModel.lng = marker.position.longitude
        runModel.zoom = map.cameraPosition.zoom
    }

    override fun onMarkerDragStart(marker: Marker) {
        runModel.lat += marker.position.latitude
        runModel.lng += marker.position.longitude
        runModel.zoom = map.cameraPosition.zoom
        pLines.add(LatLng(marker.position.latitude, marker.position.longitude))
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val loc = LatLng(runModel.lat, runModel.lng)
        val option = PolylineOptions().add(loc)
        map.addPolyline(option)
        marker.snippet = "GPS : $loc"
        return true
    }
}
