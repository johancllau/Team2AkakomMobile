package com.neox.neoxdev.ecommerce.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.neox.neoxdev.ecommerce.R
import com.neox.neoxdev.ecommerce.util.SharedPreferencesStorage
import com.neox.neoxdev.ecommerce.viewmodel.LoginActivityViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginActivityViewModel: LoginActivityViewModel

    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginActivityViewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)

        validateLogin()

        observeData()
    }

    private fun observeData() {
        loginActivityViewModel.login().observe(this, Observer {

            if (it != null) {
                if (it.data != null) {
                    val sharedPreferences = SharedPreferencesStorage(this)

                    sharedPreferences.login = true
                    sharedPreferences.userData = Gson().toJson(it.data.profile)

                    progressBarLogin.visibility = View.GONE
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()

                } else {
                    progressBarLogin.visibility = View.GONE
                    textViewEmailPasswordWrong.visibility = View.VISIBLE

                    textInputLayoutEmailLogin.error = getString(R.string.text_label_email_invalid)
                    textInputLayoutEmailLogin.errorIconDrawable = null
                    textInputLayoutPasswordLogin.error = getString(R.string.text_label_password_empty)
                    textInputLayoutPasswordLogin.errorIconDrawable = null

                    textInputLayoutEmailLogin.clearFocus()
                    textInputLayoutPasswordLogin.clearFocus()
                }

            } else {
                progressBarLogin.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Please make sure your internet connect to internet",
                    Toast.LENGTH_LONG
                ).show()

                textInputLayoutEmailLogin.clearFocus()
                textInputLayoutPasswordLogin.clearFocus()

            }

        })
    }

    private fun validateLogin() {
        textInputLayoutEmailLogin.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (isEmailValid(p0.toString().trim())) {
                    email = p0.toString()

                    textInputLayoutEmailLogin.error = null
                    textViewLabelInvalidEmail.visibility = View.GONE
                } else {
                    textInputLayoutEmailLogin.error = getString(R.string.text_label_email_invalid)
                    textViewLabelInvalidEmail.visibility = View.VISIBLE
                }
            }
        })

        textInputLayoutPasswordLogin.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && p0.trim() != "") {
                    password = p0.toString()

                    textInputLayoutPasswordLogin.error = null
                    textViewLabelInvalidPassword.visibility = View.GONE
                } else {
                    textInputLayoutPasswordLogin.error = getString(R.string.text_label_password_empty)
                    textViewLabelInvalidPassword.visibility = View.VISIBLE
                }
            }
        })

        buttonLogin.setOnClickListener {
                if (email != null ) {
                    if(password != null) {
                        textViewEmailPasswordWrong.visibility = View.GONE
                        progressBarLogin.visibility = View.VISIBLE

                        loginActivityViewModel.userLogin(email!!, password!!)
                    } else {
                        textInputLayoutPasswordLogin.error = getString(R.string.text_label_password_empty)
                        textViewLabelInvalidPassword.visibility = View.VISIBLE
                    }
                } else {
                    textInputLayoutEmailLogin.error = getString(R.string.text_label_email_invalid)
                    textViewLabelInvalidEmail.visibility = View.VISIBLE
                }
        }

    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(email)
    }
}
