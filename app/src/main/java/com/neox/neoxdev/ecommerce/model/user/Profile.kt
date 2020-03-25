package com.neox.neoxdev.ecommerce.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Profile(
    @Expose
    @SerializedName("email")
    val email: String,

    @Expose
    @SerializedName("id")
    val id: String,

    @Expose
    @SerializedName("last_login")
    val lastLogin: String,

    @Expose
    @SerializedName("nama")
    val nama: String
)