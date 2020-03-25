package com.neox.neoxdev.ecommerce.service.network


import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    companion object {
        private const val base_url = "https://neoxpkl.herokuapp.com/auth/"
        fun create() : UserApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            return retrofit.create(UserApi::class.java)
        }
    }
}