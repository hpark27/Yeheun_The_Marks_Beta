package com.themarks.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.themarks.android.databinding.ActivityNoticeeditBinding
import com.themarks.android.databinding.NoticeeditBinding

class NoticeEditActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNoticeeditBinding
    // firebase firestore
    private var fireStore: FirebaseFirestore? = null
    // snapshot
    private var snapshot: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeeditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fireStore = FirebaseFirestore.getInstance()

        binding.noticeRe.adapter = ViewAdapter()
        binding.noticeRe.layoutManager = LinearLayoutManager(this)

        // back
        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        snapshot?.remove()
    }

    inner class ViewAdapter : RecyclerView.Adapter<NoticeEditActivity.ViewAdapter.ViewHolder>(){
        // array lists for content informations
        private val contents: ArrayList<Notice> = ArrayList()

        init{
            fireStore?.collection("Notice")?.get()
                    ?.addOnSuccessListener {result ->
                        contents.clear()
                        for(document in result){
                            val item = Notice(document["title"] as String?,
                                    document["content"] as String?, document["date"] as String?)

                            contents.add(item)
                        }
                        notifyDataSetChanged()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(applicationContext, getString(R.string.submain_bulletin_fail), Toast.LENGTH_SHORT).show()
                    }
        }

        inner class ViewHolder(val binding: NoticeeditBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(NoticeeditBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // title
            val title = contents[position].title.toString()
            // date
            val date = contents[position].date.toString()
            // get fileName
            val fileName = "$title $date"
            // title
            holder.binding.title.text = title
            // content
            holder.binding.content.text = contents[position].content

            // delete file
            holder.binding.delete.setOnClickListener {
                fireStore?.collection("Notice")?.document(fileName)?.delete()?.addOnSuccessListener {
                    Toast.makeText(applicationContext,getString(R.string.deleted), Toast.LENGTH_SHORT).show()
                    finish()
                }?.addOnFailureListener {
                    Toast.makeText(applicationContext, getString(R.string.submain_bulletin_fail), Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun getItemCount(): Int {
            return contents.size
        }
    }
}