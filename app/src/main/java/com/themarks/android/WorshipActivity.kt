package com.themarks.android

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.themarks.android.databinding.ActivityWorshipBinding

class WorshipActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWorshipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorshipBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // back
        binding.back.setOnClickListener{
            finish()
        }

        //open Google map
        binding.mapExport.setOnClickListener {
            googleMapOpen()
        }

        // church map
        binding.facilityExport.setOnClickListener {
            val facility = Intent(this, FacilityActivity::class.java)

            startActivity(facility)
        }
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