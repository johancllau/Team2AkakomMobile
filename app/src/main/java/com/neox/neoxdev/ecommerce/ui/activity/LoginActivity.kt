package com.neox.neoxdev.ecommerce.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.neox.neoxdev.ecommerce.R
import com.neox.neoxdev.ecommerce.model.user.Profile
import com.neox.neoxdev.ecommerce.util.SharedPreferencesStorage
import com.neox.neoxdev.ecommerce.util.toast
import com.neox.neoxdev.ecommerce.viewmodel.LoginActivityViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    
    companion object {
        private const val SIGN_IN_GOOGLE_REQUEST_CODE = 101
    }

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var loginActivityViewModel: LoginActivityViewModel

    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        loginActivityViewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)

        validateLogin()
        observeData()

    }

    private fun observeData() {
        loginActivityViewModel.login().observe(this, Observer {
            showProgress(false)
            
            if (it != null) {
                if (it.data != null) {
                    val sharedPreferences = SharedPreferencesStorage(this)
                    sharedPreferences.login = true
                    sharedPreferences.userData = Gson().toJson(it.data.profile)
                    
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()

                } else {
                    showTextEmailPassIncorrect(true)
                    textInputLayoutEmailLogin.error = getString(R.string.text_label_email_invalid)
                    textInputLayoutEmailLogin.errorIconDrawable = null
                    textInputLayoutPasswordLogin.error = getString(R.string.text_label_password_empty)
                    textInputLayoutPasswordLogin.errorIconDrawable = null

                    textInputLayoutEmailLogin.clearFocus()
                    textInputLayoutPasswordLogin.clearFocus()
                }
            } else {
                getString(R.string.text_internet_not_connect).toast(this)
                
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
                if (isValidEmail(p0.toString().trim())) {
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
                        showTextEmailPassIncorrect(false)
                        showProgress(true)

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

        buttonLoginGoogle.setOnClickListener {
            showProgress(true)
            startActivityForResult(googleSignInClient.signInIntent, SIGN_IN_GOOGLE_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_GOOGLE_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    firebaseAuthWithGoogle(it)
                }
            } catch (e: ApiException) {
                showProgress(false)
                getString(R.string.text_internet_not_connect).toast(this)
               e.printStackTrace()
            }
        }
    }

    private fun firebaseAuthWithGoogle(userAccount: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(userAccount.idToken, null)
        val auth = FirebaseAuth.getInstance()

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {task ->
                showProgress(false)
                if (task.isSuccessful) {
                    val id = auth.currentUser?.uid
                    val email = auth.currentUser?.email
                    val name = auth.currentUser?.displayName

                    if (id != null && email != null && name != null) {
                        val profile = Profile(
                            email,
                            id,
                            "25-02-2020",
                            name
                        )
                        val sharedPreferences = SharedPreferencesStorage(this)

                        sharedPreferences.login = true
                        sharedPreferences.userData = Gson().toJson(profile)
                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )
                        finish()
                    } else {
                        "Login failed".toast(this)
                    }
                } else {
                    "Login failed".toast(this)
                }
            }
            .addOnFailureListener {
                showProgress(false)
                it.printStackTrace()
            }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(email)
    }

    private fun showTextEmailPassIncorrect(show: Boolean) {
        if (show) {
            textViewEmailPasswordWrong.visibility = View.VISIBLE
        } else {
            textViewEmailPasswordWrong.visibility = View.GONE
        }
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            progressBarLogin.visibility = View.VISIBLE
        } else {
            progressBarLogin.visibility = View.GONE
        }
    }
}
