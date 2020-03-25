package com.neox.neoxdev.ecommerce.model.user


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @Expose
    @SerializedName("data")
    val `data`: Data? = null,

    @Expose
    @SerializedName("message")
    val message: String
)