package com.themarks.android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.themarks.android.databinding.ActivityNoticeuploadBinding
import java.text.SimpleDateFormat
import java.util.*

class NoticeUploadActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNoticeuploadBinding
    // firebase firestore
    private lateinit var fireStore: FirebaseFirestore
    // posting title
    private lateinit var title: String
    // posting content
    private lateinit var content: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeuploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initialize firestore
        fireStore = FirebaseFirestore.getInstance()

        // go back to posting page
        binding.back.setOnClickListener {
            finish()
        }

        // upload bulletin
        binding.upload.setOnClickListener {
            // get title and content
            title = binding.name.text.toString()
            content = binding.content.text.toString()

            when {
                title.isEmpty() -> {
                    Toast.makeText(applicationContext, getString(R.string.noticeupload_name), Toast.LENGTH_SHORT).show()
                    binding.name.requestFocus()
                }
                content.isEmpty() -> { // when content is empty
                    Toast.makeText(applicationContext, getString(R.string.noticeupload_content), Toast.LENGTH_SHORT).show()
                    binding.content.requestFocus()
                }
                else -> {
                    upload(title, content)
                }
            }
        }
    }

    /**
     * Upload announce on firebasestore
     */
    private fun upload(title: String, content: String){
        // current date
        val currentDate = Calendar.getInstance().time
        // convert date into string
        val date = SimpleDateFormat("yyyy. MM. dd", Locale.getDefault()).format(currentDate)
        // firestore document name
        val fileName = "$title $date"

        val noticeMap = hashMapOf(
                "title" to title,
                "content" to content,
                "date" to date)

        fireStore.collection("Notice").document(fileName).set(noticeMap)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, getString(R.string.uploaded), Toast.LENGTH_SHORT).show()
                    // clear edit text and request focus
                    binding.name.text = null
                    binding.content.text = null
                    binding.name.requestFocus()
                }
    }
}