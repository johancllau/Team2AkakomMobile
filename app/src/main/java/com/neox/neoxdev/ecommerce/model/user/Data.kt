package com.neox.neoxdev.ecommerce.model.user


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
    @Expose
    @SerializedName("profile")
    val profile: Profile,

    @Expose
    @SerializedName("token")
    val token: String
)