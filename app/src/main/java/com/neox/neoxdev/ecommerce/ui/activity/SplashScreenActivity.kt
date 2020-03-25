package com.neox.neoxdev.ecommerce.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.neox.neoxdev.ecommerce.R
import com.neox.neoxdev.ecommerce.util.SharedPreferencesStorage

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val sharedPreferences = SharedPreferencesStorage(this)

        Handler().postDelayed({
            if (sharedPreferences.login) {
                startActivity(
                    Intent(this, MainActivity::class.java)
                )
                finish()
            } else {
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finish()
            }
        }, 1500)
    }
}
