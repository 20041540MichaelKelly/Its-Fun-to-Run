package mick.studio.itsfuntorun.ui.map

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import mick.studio.itsfuntorun.R
import timber.log.Timber

@SuppressLint("MissingPermission")
class MapsViewModel(application: Application) : AndroidViewModel(application), GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener  {

    lateinit var map : GoogleMap
    private val latLngs = MutableLiveData<ArrayList<LatLng>>()
    private lateinit var latLngList : LatLng
    val observableLatLngs: LiveData<ArrayList<LatLng>>
        get() = latLngs

    var currentLocation = MutableLiveData<Location>()
    var locationClient : FusedLocationProviderClient
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(5000)
        .setMaxUpdateDelayMillis(15000)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            currentLocation.value = locationResult.locations.last()
        }
    }

    init {
        locationClient = LocationServices.getFusedLocationProviderClient(application)
        locationClient.requestLocationUpdates(locationRequest, locationCallback,
            Looper.getMainLooper())
    }

    fun updateCurrentLocation() {
        if(locationClient.lastLocation.isSuccessful)
            locationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    currentLocation.value = location!!
                    moveMarker(LatLng(currentLocation.value!!.latitude, currentLocation.value!!.longitude))
                    Timber.i("MAP VM LOC SUCCESS: %s", currentLocation.value)
                }
        else // Couldn't get Last Location
            currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        Timber.i("MAP VM LOC : %s", currentLocation.value)
    }

    fun addToPolyLineLatLng(latLng: LatLng) {
        latLngs.value?.add(latLng)

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val option = PolylineOptions().add(latLngList)
        map.addPolyline(option)
        marker.snippet = "GPS : $latLngList"
        return true
    }

    override fun onMarkerDragEnd(marker: Marker) {
//        latLngList.lat = marker.position.latitude
//        runModel.lng = marker.position.longitude
//        runModel.zoom = map.cameraPosition.zoom
    }

    override fun onMarkerDragStart(marker: Marker) {
//        runModel.lat += marker.position.latitude
//        runModel.lng += marker.position.longitude
//        runModel.zoom = map.cameraPosition.zoom
//        latLngList.add(LatLng(marker.position.latitude, marker.position.longitude))
    }


    override fun onMarkerDrag(marker: Marker) {
        if (onMarkerClick(marker)) {
            onMarkerDragStart(marker)
        }
    }

    private fun moveMarker(latLng: LatLng) {
        val options = MarkerOptions()
            .title("Run")
            .snippet("GPS : $latLng")
            .draggable(true)
            .position(latLng)
            .icon(bitmapDescriptorFromVector(getApplication<Application>().applicationContext, R.drawable.baseline_directions_run_24))

        map.setOnMarkerDragListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

}

