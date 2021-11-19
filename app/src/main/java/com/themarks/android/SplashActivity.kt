package com.themarks.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val main = Intent(this, MainActivity::class.java)
        // run intent
        startActivity(main)
        // finish splash activity so that it does not appear after moving to main activity
        finish()
    }
}