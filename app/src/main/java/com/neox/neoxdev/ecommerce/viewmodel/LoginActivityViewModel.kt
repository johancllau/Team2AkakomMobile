package com.neox.neoxdev.ecommerce.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neox.neoxdev.ecommerce.model.user.LoginResponse
import com.neox.neoxdev.ecommerce.service.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivityViewModel : ViewModel() {

    private val retrofitService = RetrofitService.create()

    private var responseLogin = MutableLiveData<LoginResponse>()

    fun login() : MutableLiveData<LoginResponse> {

        return responseLogin
    }

    fun userLogin(email: String, password: String) {

        retrofitService.userLogin(email, password).enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    responseLogin.postValue(null)
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        responseLogin.postValue(response.body())
                    } else {
                        Log.i("error response : ", response.errorBody().toString())
                    }
                }
        })
    }
}