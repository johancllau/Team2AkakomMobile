package com.neox.neoxdev.ecommerce.service.repository

import android.util.Log

import com.neox.neoxdev.ecommerce.model.user.LoginResponse
import com.neox.neoxdev.ecommerce.service.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class UserRepository {

//    private val retrofitService = RetrofitService.create()
//
//    fun userLogin(email: String, password: String) : LoginResponse? {
//        var loginResponse: LoginResponse? = null
//
//        retrofitService.userLogin(email, password)
//            .enqueue(object : Callback<LoginResponse> {
//                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                    Log.i("Error failure : ", t.message.toString())
//                }
//
//                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                    if (response.isSuccessful) {
//                        Log.i("error : ", "success : ${response.body().toString()}")
//                        loginResponse = response.body()
//                    } else {
//                        Log.i("Error response : ", response.errorBody().toString())
//                    }
//
//                }
//            })
//        Log.i("error : ", "value : $loginResponse")
//        return loginResponse
//    }
//}