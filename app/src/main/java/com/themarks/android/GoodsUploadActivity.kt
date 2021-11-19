package com.themarks.android

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.themarks.android.databinding.ActivityGoodsuploadBinding

class GoodsUploadActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGoodsuploadBinding
    private var url: String = ""
    // firebase download url
    private var downloadUrl: String = ""
    // firebase firestore
    private lateinit var fireStore: FirebaseFirestore

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            url = it.data?.data.toString()

            if(url.isEmpty()){
                Toast.makeText(applicationContext, getString(R.string.submain_bulletin_fail), Toast.LENGTH_SHORT).show()
            }else{
                // show image on imageview
                Glide.with(this).load(url).into(binding.profile)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoodsuploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initialize Firebase Database
        fireStore = FirebaseFirestore.getInstance()

        // back
        binding.back.setOnClickListener {
            finish()
        }

        // pick image
        binding.profile.setOnClickListener {
            val pickImage = Intent(Intent.ACTION_PICK)
            pickImage.type="image/*"

            getContent.launch(pickImage)
        }

        // upload post
        binding.upload.setOnClickListener {
            val fileName = binding.name.text.toString()
            val price = binding.price.text.toString()

            when {
                fileName.isEmpty() -> {
                    Toast.makeText(applicationContext, getString(R.string.goodsupload_name), Toast.LENGTH_SHORT).show()
                    binding.name.requestFocus()
                }
                price.isEmpty() -> {
                    Toast.makeText(applicationContext, getString(R.string.goodsupload_price), Toast.LENGTH_SHORT).show()
                    binding.price.requestFocus()
                }
                else -> {
                    upload(fileName, price)
                }
            }
        }
    }

    /**
     *  Upload post
     */
    private fun upload(fileName: String, price: String){
        // get firebase storage reference
        val ref = FirebaseStorage.getInstance().getReference("Goods/$fileName")
        // upload task
        val upload: StorageTask<*>

        if(url.isNotEmpty()){
            val uri = Uri.parse(url)
            upload = ref.putFile(uri)

            upload.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if(!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                // get image download url from firebase
                downloadUrl = task.result.toString()

                val goodsMap = hashMapOf(
                        "name" to fileName,
                        "price" to price,
                        "image" to downloadUrl)

                fireStore.collection("Goods").document(fileName).set(goodsMap)
                        .addOnCompleteListener {
                            Toast.makeText(applicationContext, getString(R.string.uploaded), Toast.LENGTH_SHORT).show()

                            binding.name.text = null
                            binding.price.text = null
                            binding.profile.setImageResource(android.R.color.transparent)
                            binding.name.requestFocus()
                        }
            }
        }else{
            Toast.makeText(applicationContext, getString(R.string.goodsupload_image), Toast.LENGTH_SHORT).show()
        }
    }
}