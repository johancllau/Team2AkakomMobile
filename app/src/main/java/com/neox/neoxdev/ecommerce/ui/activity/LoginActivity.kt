package com.neox.neoxdev.ecommerce.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.neox.neoxdev.ecommerce.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {

            val email = editTextEmailLogin.text.toString().trim()
            val password = editTextPasswordLogin.text.toString().trim()

            if (email.isEmpty() || email.isBlank()) {
                editTextEmailLogin.error = getString(R.string.text_label_email_empty)
                editTextEmailLogin.requestFocus()
            } else if (password.isEmpty() || password.isBlank()) {
                editTextPasswordLogin.error = getString(R.string.text_label_password_empty)
                editTextPasswordLogin.requestFocus()
            } else {
                textViewEmailPasswordWrong.visibility = View.GONE
                progressBarLogin.visibility = View.VISIBLE

                if (isEmailValid(email)) {
                    if (email == "admin@gmail.com" && password == "admin123") {
                        Toast.makeText(
                            this,
                            "Login success",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )
                        finish()
                    } else {
                        editTextEmailLogin.text = null
                        editTextPasswordLogin.text = null
                        editTextEmailLogin.requestFocus()

                        progressBarLogin.visibility = View.GONE
                        textViewEmailPasswordWrong.visibility = View.VISIBLE
                    }
                } else {
                    progressBarLogin.visibility = View.GONE
                    editTextEmailLogin.error = getString(R.string.text__label_email_invalid)
                    editTextEmailLogin.requestFocus()
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(email)
    }
}
