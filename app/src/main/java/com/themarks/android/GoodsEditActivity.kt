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
import com.themarks.android.databinding.ActivityGoodseditBinding
import com.themarks.android.databinding.GoodseditBinding

class GoodsEditActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGoodseditBinding
    // firebase firestore
    private var fireStore: FirebaseFirestore? = null
    // snapshot
    private var snapshot: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoodseditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fireStore = FirebaseFirestore.getInstance()

        binding.goodsRe.adapter = ViewAdapter()
        binding.goodsRe.layoutManager = LinearLayoutManager(this)

        // back
        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        snapshot?.remove()
    }

    inner class ViewAdapter : RecyclerView.Adapter<GoodsEditActivity.ViewAdapter.ViewHolder>(){
        // array lists for content informations
        private val contents: ArrayList<Goods> = ArrayList()

        init{
            fireStore?.collection("Goods")?.get()
                    ?.addOnSuccessListener {result ->
                        contents.clear()
                        for(document in result){
                            val item = Goods(document["name"] as String?, document["price"] as String?,
                                    document["remain"] as String?)

                            contents.add(item)
                        }
                        notifyDataSetChanged()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(applicationContext, getString(R.string.submain_bulletin_fail), Toast.LENGTH_SHORT).show()
                    }
        }

        inner class ViewHolder(val binding: GoodseditBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(GoodseditBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // title
            val fileName = contents[position].name.toString()

            holder.binding.name.text = fileName

            // delete file
            holder.binding.delete.setOnClickListener {
                fireStore?.collection("Goods")?.document(fileName)?.delete()?.addOnSuccessListener {
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