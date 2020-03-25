package com.neox.neoxdev.ecommerce.service.network

import com.neox.neoxdev.ecommerce.model.user.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

}