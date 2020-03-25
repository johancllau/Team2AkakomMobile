package com.neox.neoxdev.ecommerce.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.neox.neoxdev.ecommerce.R
import com.neox.neoxdev.ecommerce.model.user.Profile
import com.neox.neoxdev.ecommerce.util.SharedPreferencesStorage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferencesStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbarHome)

        sharedPreferences = SharedPreferencesStorage(this)
        if (sharedPreferences.login) {
            val dataUser = Gson().fromJson(sharedPreferences.userData, Profile::class.java)
            textEmail.text = dataUser.email
            textName.text = dataUser.nama
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menuLogOut) logOut()

        return super.onOptionsItemSelected(item)
    }

    private fun logOut() {
        sharedPreferences.login = false
        sharedPreferences.userData = ""

        startActivity(
            Intent(this, LoginActivity::class.java)
        )
        this.finish()
    }
}
