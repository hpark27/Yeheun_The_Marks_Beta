package com.themarks.android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.themarks.android.databinding.ActivityAlarmuploadBinding

class AlarmUploadActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAlarmuploadBinding
    // posting content
    private lateinit var content: String
    private lateinit var dataBase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmuploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataBase = FirebaseDatabase.getInstance()

        // go back to posting page
        binding.back.setOnClickListener {
            finish()
        }

        // upload notice
        binding.upload.setOnClickListener {
            content = binding.content.text.toString()

            if(content.isEmpty()){
                Toast.makeText(applicationContext,getString(R.string.noticeupload_content),Toast.LENGTH_SHORT).show()
                binding.content.requestFocus()
            }else{
                // reference to firebase database. Specific user database (*uid)
                dataBase.reference.child("Admin").child("Alarm").setValue(content).
                addOnCompleteListener {
                    Toast.makeText(applicationContext,getString(R.string.uploaded), Toast.LENGTH_SHORT).show()
                    binding.content.text=null
                }
            }
        }
    }
}