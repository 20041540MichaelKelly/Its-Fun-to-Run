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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.CancellationTokenSource
import mick.studio.itsfuntorun.R
import mick.studio.itsfuntorun.databinding.ActivityMapsBinding
import mick.studio.itsfuntorun.models.Location

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener,
    OnLocationChangedListener{

    //2
    private val locations = mutableListOf<LatLng>()

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var location = Location()
    private var listOfLatLng = mutableListOf<LatLng>()
    private val LOCATION_REQUEST_CODE = 101

    private var currentLocation: Location? = null


    //This allows an option for the user to cancel
    private var cancellationTokenSource = CancellationTokenSource()

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
        //location = intent.extras?.getParcelable<Location>("location")!!
        // TODO: Step 1.3, Create a LocationRequest.

        //No.2
        if(checkUserPermission()) { // I call this to ensure that permission have been accepted
            getLocation()
        }
    }

    //No.5
    override fun onMapReady(googleMap: GoogleMap) {
        val lat_lng = LatLng(location.lat, location.lng)

        map = googleMap
        val options = MarkerOptions()
            .title("Run")
            .snippet("GPS : $lat_lng")
            .draggable(true)
            .position(lat_lng)
            .icon(bitmapDescriptorFromVector(this, R.drawable.baseline_directions_run_24))
        map.addMarker(options)
        map.setOnMarkerDragListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_lng, location.zoom))
    }

    //No.3
    private fun getLocation() {
        //checkUserPermission()
        mapSetup()
    }

    //No.4
    @SuppressLint("MissingPermission")
    private fun mapSetup() {
        /*fusedLocationClient.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )*/
        fusedLocationClient.lastLocation.addOnSuccessListener { task ->
                location.lat = task.latitude
                location.lng = task.longitude
                location.zoom = 15f

                locations.add(LatLng(task.latitude, task.longitude))
                setUptheMap()
            }

    }

    private fun setUptheMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //No.3
    fun checkUserPermission() : Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return true
        }
        return false
    }
//No.2 after the permission was granted by the user
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            LOCATION_REQUEST_CODE -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
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

        drawRoute(listOfLatLng)
    }

    override fun onMarkerDragStart(marker: Marker) {
        location.lat += marker.position.latitude
        location.lng += marker.position.longitude
        location.zoom = map.cameraPosition.zoom
        listOfLatLng.add(LatLng(marker.position.latitude, marker.position.longitude))
    }

    private fun drawRoute(lat_lng: MutableList<LatLng>) {
        val polylineOptions = PolylineOptions()

        val points = polylineOptions.points
        points.addAll(lat_lng)

        map.addPolyline(polylineOptions)
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

    override fun onLocationChanged(loc: android.location.Location) {

    }
 }