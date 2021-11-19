package com.themarks.android

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.themarks.android.databinding.ActivityMapviewBinding

class MapViewActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMapviewBinding
    private lateinit var storage: StorageReference
    private lateinit var url: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapviewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        storage = FirebaseStorage.getInstance().reference

        storage.child("Map/map.jpeg").downloadUrl.addOnSuccessListener {
            url = it
            Glide.with(this).load(url).into(binding.imageView)
        }.addOnFailureListener {
            Toast.makeText(applicationContext,"Something went wrong\nPlease try again later", Toast.LENGTH_SHORT).show()
        }
    }
}