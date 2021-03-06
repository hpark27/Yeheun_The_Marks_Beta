package com.themarks.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.themarks.android.databinding.ActivityGoodsBinding
import com.themarks.android.databinding.GoodsBinding

class GoodsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGoodsBinding
    // firebase firestore
    private var fireStore: FirebaseFirestore? = null
    // snapshot
    private var snapshot: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoodsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fireStore = FirebaseFirestore.getInstance()

        binding.goodsRe.adapter = ViewAdapter()
        binding.goodsRe.layoutManager = LinearLayoutManager(this)

        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        snapshot?.remove()
    }

    inner class ViewAdapter : RecyclerView.Adapter<ViewAdapter.ViewHolder>(){
        // array lists for content informations
        private val contents: ArrayList<Goods> = ArrayList()

        init{
            fireStore?.collection("Goods")?.get()
                    ?.addOnSuccessListener {result ->
                        contents.clear()
                        for(document in result){
                            val item = Goods(document["name"] as String?, document["price"] as String?,
                                    document["image"] as String?)

                            contents.add(0,item)
                        }
                        notifyDataSetChanged()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(applicationContext, getString(R.string.submain_bulletin_fail), Toast.LENGTH_SHORT).show()
                    }
        }

        inner class ViewHolder(val binding: GoodsBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(GoodsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // url
            val url = contents[position].image
            // title
            holder.binding.name.text = contents[position].name
            // content
            holder.binding.price.text = contents[position].price

            Glide.with(holder.itemView.context).load(url).into(holder.binding.goods)
        }

        override fun getItemCount(): Int {
            return contents.size
        }
    }
}