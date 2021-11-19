package com.themarks.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.themarks.android.databinding.ActivityEditBinding

class EditActivity: AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // back
        binding.back.setOnClickListener {
            val submain = Intent(this, SubMainActivity::class.java)
            startActivity(submain)
            finish()
        }

        // goods edit
        binding.goods.setOnClickListener {
            val goods = Intent(this, GoodsEditActivity::class.java)
            startActivity(goods)
        }

        binding.notice.setOnClickListener {
            val notice = Intent(this, NoticeEditActivity::class.java)
            startActivity(notice)
        }
    }

    // back button override
    override fun onBackPressed() {
        super.onBackPressed()

        val submain = Intent(this, SubMainActivity::class.java)
        startActivity(submain)
        finishAffinity()
    }
}