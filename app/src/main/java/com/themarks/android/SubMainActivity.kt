package com.themarks.android

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.themarks.android.databinding.ActivitySubmainBinding

class SubMainActivity:AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivitySubmainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var pdfUri: String
    private lateinit var alarmContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initialize navigation drawer
        binding.mainNavigation.setNavigationItemSelectedListener(this)

        // initialize authentication
        auth = FirebaseAuth.getInstance()

        // set alarm symbol invisible
        binding.alarm.visibility= View.INVISIBLE

        // reference to firebase database
        val ref = FirebaseDatabase.getInstance().getReference("Admin").child("Alarm")

        ref.get().addOnSuccessListener {
            alarmContent = it.value.toString()

            if(alarmContent.isNotEmpty()){
                if(alarmContent!="null"){
                    // set alarm symbol visible
                    binding.alarm.visibility= View.VISIBLE
                }
            }
        }

        // open drawer
        binding.menu.setOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }

        // show alarm
        binding.alarm.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                    .setIcon(R.drawable.themarks)
                    .setMessage(alarmContent)
            builder.setPositiveButton(R.string.submain_dialog, DialogInterface.OnClickListener{ dialog, which ->
                dialog.dismiss()
            })
            builder.create()
            builder.show()
        }

        // worship
        binding.worship.setOnClickListener {
            val worship = Intent(this, WorshipActivity::class.java)
            startActivity(worship)
        }

        // read bulletin
        binding.bulletin.setOnClickListener {
            readPdf()
        }

        // open google form
        binding.register.setOnClickListener {
            openGoogleform()
        }

        // offering
        binding.offering.setOnClickListener {
            val offering = Intent(this, OfferingActivity::class.java)
            startActivity(offering)
        }

        // goods
        binding.goods.setOnClickListener {
            val goods = Intent(this, GoodsActivity::class.java)
            startActivity(goods)
        }

        // notice
        binding.notice.setOnClickListener {
            val notice = Intent(this, NoticeActivity::class.java)
            startActivity(notice)
        }

        // Attribute click characterlistic on bottom navigation menu items
        binding.botNavigation.setOnItemReselectedListener {
            when(it.itemId){
                // open youtube channel
                R.id.youtube -> {
                    openYoutube()
                    return@setOnItemReselectedListener
                }
                // open instagram channel
                R.id.instagram -> {
                    openInstagram()
                    return@setOnItemReselectedListener
                }
            }

            return@setOnItemReselectedListener
        }
    }

    /**
     * Navigation menu item is selected
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit -> loginEdit()

            R.id.upload -> loginUpload()

            R.id.logout -> logOut()
        }

        // close navigation bar
        binding.mainDrawer.closeDrawers()

        return true
    }

    /**
     *  Get Pdf Url
     */
    private fun readPdf(){
        // reference to firebase database
        val ref = FirebaseDatabase.getInstance().getReference("Admin").child("Bulletin")

        ref.get().addOnSuccessListener {
            pdfUri = it.value.toString()

            if(pdfUri.isEmpty()){
                Toast.makeText(applicationContext, getString(R.string.submain_bulletin_empy), Toast.LENGTH_SHORT).show()
            }else{
                val read = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUri))
                startActivity(read)
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, getString(R.string.submain_bulletin_fail), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     *  Open Google Form
     */
    private fun openGoogleform(){
        // reference to firebase database
        val ref = FirebaseDatabase.getInstance().getReference("Admin").child("Application")

        ref.get().addOnSuccessListener {
            pdfUri = it.value.toString()

            if(pdfUri.isEmpty()){
                Toast.makeText(applicationContext,getString(R.string.submain_googleform_empy), Toast.LENGTH_SHORT).show()
            }else{
                val read = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUri))
                startActivity(read)
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext,getString(R.string.submain_googleform_fail), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     *  Go to login activity
     */
    private fun loginEdit(){
        val login = Intent(this, LoginActivity::class.java)
        login.putExtra("Login",1)
        startActivity(login)
    }

    /**
     *  Go to login activity
     */
    private fun loginUpload(){
        val login = Intent(this, LoginActivity::class.java)
        login.putExtra("Login",2)
        startActivity(login)
    }

    /**
     * Logout intent
     */
    private fun logOut(){
        // get current user
        var user = auth.currentUser

        if(user!=null){
            auth.signOut()

            // get current user
            user = auth.currentUser

            if(user == null){
                Toast.makeText(applicationContext,"Logout successful",Toast.LENGTH_SHORT).show()
            }else{
                Toast.
                makeText(applicationContext,"Something went wrong\nPlease try again",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(applicationContext,"Please sign in first",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Open Youtube App
     */
    private fun openYoutube(){
        // get string from string resource
        val themarks = getString(R.string.lolmc_youtube)
        // get uri from string
        val themarksUri: Uri = Uri.parse(themarks)
        // intent
        val themarksIntent = Intent(Intent.ACTION_VIEW,themarksUri)
        // app package name
        val youtubePackage = getString(R.string.youtube_package)
        // app chooser title
        val appOpenTitle: String = getString(R.string.app_open)
        themarksIntent.setPackage(youtubePackage)
        // try catch with youtube package

        try {
            // when youtube app is installed
            startActivity(themarksIntent)
        } catch (e: ActivityNotFoundException) { // when youtube app does not exist
            // webview intent
            val themarksWebIntent = Intent(Intent.ACTION_VIEW, themarksUri)
            // create app chooser
            val appChooserYoutube: Intent =
                Intent.createChooser(themarksWebIntent, appOpenTitle)
            startActivity(appChooserYoutube)
        }
    }

    /**
     * Open Instagram App
     */
    private fun openInstagram(){
        // get string from string resource
        val instagram = getString(R.string.lolmc_instagram)
        // get uri from string
        val instagramUri: Uri = Uri.parse(instagram)
        // intent
        val instagramIntent = Intent(Intent.ACTION_VIEW, instagramUri)
        // instagram package name
        val instagramPackage = getString(R.string.instagram_package)
        // app chooser title
        val appOpenTitle: String = getString(R.string.app_open)
        instagramIntent.setPackage(instagramPackage)
        // try catch with youtube package
        try {
            // when youtube app is installed
            startActivity(instagramIntent)
        } catch (e: ActivityNotFoundException) { // when youtube app does not exist
            // webview intent
            val instagramWebIntent = Intent(Intent.ACTION_VIEW, instagramUri)
            // create app chooser
            val appChooserInstagram: Intent =
                Intent.createChooser(instagramWebIntent, appOpenTitle)
            startActivity(appChooserInstagram)
        }
    }
}
