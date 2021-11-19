package com.themarks.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.themarks.android.databinding.ActivityUploadBinding

class UploadActivity: AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // back
        binding.back.setOnClickListener {
            val submain = Intent(this, SubMainActivity::class.java)
            startActivity(submain)
            finish()
        }

        // url
        binding.url.setOnClickListener {
            val url = Intent(this, UrlUploadActivity::class.java)
            startActivity(url)
        }

        // offering
        binding.offering.setOnClickListener {
            val offering = Intent(this, OfferingUploadActivity::class.java)
            startActivity(offering)
        }

        // goods
        binding.goods.setOnClickListener {
            val goods = Intent(this, GoodsUploadActivity::class.java)
            startActivity(goods)
        }

        // notice
        binding.notice.setOnClickListener {
            val notice = Intent(this, NoticeUploadActivity::class.java)
            startActivity(notice)
        }

        // alarm
        binding.alarm.setOnClickListener{
            val alarm = Intent(this, AlarmUploadActivity::class.java)
            startActivity(alarm)
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
