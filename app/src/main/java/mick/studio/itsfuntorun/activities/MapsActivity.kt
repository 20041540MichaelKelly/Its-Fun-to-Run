package mick.studio.itsfuntorun.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.ActivityMapsBinding
import mick.studio.itsfuntorun.models.Location

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener{
    var locationRequest = createLocationRequest()

    var requestingLocationUpdates = false
    var isMapReady = false
    val polylineOptions = PolylineOptions()

    // globally declare LocationCallback
    private lateinit var locationCallback: LocationCallback

    //2
    private val locations = mutableListOf<LatLng>()

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var location = Location()
    private var listOfLatLng = mutableListOf<LatLng>()
    private var LOCATION_REQUEST_CODE = 101
    private var myMarker : Marker? = null

    /**
     * A Lazy method is used here so it is only called when needed
     * No.1
     */
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getContinousLocation()

        //location = intent.extras?.getParcelable<Location>("location")
        //No.2
        if (checkUserPermission()) { // I call this to ensure that permission have been accepted
            mapSetup()
            startLocationUpdates()
        }
    }

    private fun getContinousLocation() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    myMarker?.position = LatLng(location.latitude, location.longitude)
                    map.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                    drawRoute(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    //No.5
    override fun onMapReady(googleMap: GoogleMap) {
        val lat_lng = LatLng(location.lat, location.lng)
        map = googleMap
        moveMarker(lat_lng)
    }

        //override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    private fun moveMarker(latLng: LatLng) {
        val options = MarkerOptions()
            .title("Run")
            .snippet("GPS : $latLng")
            .draggable(true)
            .position(latLng)
            .icon(bitmapDescriptorFromVector(this, R.drawable.baseline_directions_run_24))

        myMarker = map.addMarker(options)
        map.setOnMarkerDragListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, location.zoom))
    }

    //No.3
    private fun getLocation() {
        mapSetup()
    }

    //No.4
    @SuppressLint("MissingPermission")
    private fun mapSetup() {
        fusedLocationClient.lastLocation.addOnSuccessListener { task ->
            location.lat = task.latitude
            location.lng = task.longitude
            location.zoom = 15f

            locations.add(LatLng(task.latitude, task.longitude))
            setUpTheMap()
        }
    }

    private fun setUpTheMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        isMapReady = true
    }

    //No.3
    fun checkUserPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return false
        }
        requestingLocationUpdates= true
        return true
    }

    //first bp
    fun createLocationRequest() : LocationRequest {
         val locationRequest = LocationRequest.create()
        //Instantiating the Location request and setting the priority and the interval I need to update the location.
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest
    }

    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates && isMapReady) startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    //No.2 after the permission was granted by the user
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapSetup()
                startLocationUpdates()
            }
        }
    }

    override fun onMarkerDrag(marker: Marker) {
        if (onMarkerClick(marker)) {
            onMarkerDragStart(marker)
        }
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

    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
    }

    override fun onMarkerDragStart(marker: Marker) {
        location.lat += marker.position.latitude
        location.lng += marker.position.longitude
        location.zoom = map.cameraPosition.zoom
        listOfLatLng.add(LatLng(marker.position.latitude, marker.position.longitude))
    }

    private fun drawRoute(lat_lng: LatLng) {
        polylineOptions.points+=lat_lng
        if(isMapReady){ map.addPolyline(polylineOptions)}
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val loc = LatLng(location.lat, location.lng)
        val option = PolylineOptions().add(loc)
        map.addPolyline(option)
        marker.snippet = "GPS : $loc"
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }
}