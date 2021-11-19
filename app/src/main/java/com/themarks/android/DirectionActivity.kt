package com.themarks.android

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.themarks.android.databinding.ActivityDirectionBinding

class DirectionActivity: AppCompatActivity(), OnMapReadyCallback{
    private lateinit var binding: ActivityDirectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val mapFragment = SupportMapFragment.newInstance()
        
        supportFragmentManager
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit()

        mapFragment.getMapAsync(this)

        // open Google Map
        binding.shortcutLayout.setOnClickListener {
            googleMapOpen()
        }

        // back
        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val lolmc = LatLng(34.14638, -118.09286)

        googleMap.addMarker(
                MarkerOptions()
                        .position(lolmc)
                        .title(getString(R.string.lolmc))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lolmc,15f))
    }

    /**
     *  Open Googlemap
     */
    private fun googleMapOpen(){
        // app chooser title
        val appOpenTitle: String = getString(R.string.app_open)
        val mapUri = Uri.parse("geo:34.14638,-118.09286?q=Light of love mission church")
        // webpage address
        val mapWebUri = Uri.parse(getString(R.string.lolmc_googlemap))

        val mapIntent = Intent(Intent.ACTION_VIEW,mapUri)
        mapIntent.setPackage(getString(R.string.googlemap_package))

        // try catch with google map package
        try {
            // when youtube app is installed
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) { // when youtube app does not exist
            // webview intent
            val mapWebIntent = Intent(Intent.ACTION_VIEW, mapWebUri)
            // create app chooser
            val appChooserInstagram: Intent =
                    Intent.createChooser(mapWebIntent, appOpenTitle)
            startActivity(appChooserInstagram)
        }
    }
}