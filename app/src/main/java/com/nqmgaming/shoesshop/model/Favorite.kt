package com.nqmgaming.shoesshop.model

import com.google.gson.annotations.SerializedName

data class Favorite(
    @SerializedName("_id")
    val id: String,
    @SerializedName("user")
    val userId: String,
    val product: List<Product>
)
