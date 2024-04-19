package com.thanhvq.mapdrawline

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private lateinit var polyline: Polyline
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMapView = findViewById(R.id.mapView)
        mMapView.onCreate(savedInstanceState)
        mMapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add markers

        // Add markers
        val origin = LatLng(21.0413217, 105.8091877)
        val destination = LatLng(21.0280108, 105.7778243)
        mMap.addMarker(MarkerOptions().position(origin).title("Marker 1"))
        mMap.addMarker(MarkerOptions().position(destination).title("Marker 2"))

        // Move camera tới vị trí xuất phát
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 15f))

        val mapsAPI: GoogleMapAPI = RetrofitClient().getClient()!!.create(GoogleMapAPI::class.java)
        val call: Call<DirectionsResponse> = mapsAPI.getDirections(
            "${origin.latitude},${origin.longitude}",
            "${destination.latitude},${destination.longitude}",
            "AIzaSyA1PS17F6EdChXPXm3nFyL4sX-zC-iinKA"
        )
        call.enqueue(object : Callback<DirectionsResponse>{
            override fun onResponse(
                call: Call<DirectionsResponse>,
                response: Response<DirectionsResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val routes = response.body()!!.routes
                    if (!routes.isNullOrEmpty()) {
                        val polylinePoints: String = routes[0].overviewPolyline?.points!!
                        val decodedPoints: List<LatLng> = PolyUtil.decode(polylinePoints)
                        val polylineOptions = PolylineOptions()
                            .addAll(decodedPoints)
                            .width(10f)
                            .color(Color.RED)
                        polyline = mMap.addPolyline(polylineOptions)
                    }
                }
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                Log.d("THANHVQ", "${t.message}")
            }

        })
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }
}