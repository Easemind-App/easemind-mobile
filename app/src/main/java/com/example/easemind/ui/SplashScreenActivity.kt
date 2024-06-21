package com.example.easemind.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.easemind.R
import com.example.easemind.ui.authentication.AuthenticationActivity
import com.example.easemind.ui.homepage.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreenActivity, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000L)
    }
}