package com.themarks.android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.themarks.android.databinding.ActivityUrluploadBinding

class UrlUploadActivity: AppCompatActivity() {
    private lateinit var binding: ActivityUrluploadBinding
    private lateinit var dataBase: FirebaseDatabase
    // determine bulletin and appliation
    private var num: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUrluploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataBase = FirebaseDatabase.getInstance()

        // back
        binding.back.setOnClickListener {
            finish()
        }

        // upload bulletin URL
        binding.bulletinUpload.setOnClickListener {
            val url = binding.bulletinText.text.toString()
            num = 1
            uploadUrl(url,num)
        }

        // upload application URL
        binding.applicationUpload.setOnClickListener {
            val url = binding.applicationText.text.toString()
            num = 2
            uploadUrl(url,num)
        }
    }

    /**
     * Upload URL to firebase
     */
    private fun uploadUrl(url: String, num: Int){
        if(num==1){ // bulletin
            if(url.isEmpty()){
                Toast.makeText(applicationContext, getString(R.string.urlupload_empty), Toast.LENGTH_SHORT).show()
                binding.bulletinText.requestFocus()
            }else{
                // reference to firebase database. Specific user database (*uid)
                dataBase.reference.child("Admin").child("Bulletin").setValue(url).
                addOnCompleteListener {
                    Toast.makeText(applicationContext,getString(R.string.uploaded), Toast.LENGTH_SHORT).show()
                    binding.bulletinText.text=null
                }
            }
        }

        if(num==2){ // application
            if(url.isEmpty()){
                Toast.makeText(applicationContext, getString(R.string.urlupload_empty), Toast.LENGTH_SHORT).show()
                binding.bulletinText.requestFocus()
            }else{
                // reference to firebase database. Specific user database (*uid)
                dataBase.reference.child("Admin").child("Application").setValue(url).
                addOnCompleteListener {
                    Toast.makeText(applicationContext,getString(R.string.uploaded), Toast.LENGTH_SHORT).show()
                    binding.applicationText.text=null
                }
            }
        }
    }
}