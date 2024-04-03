package com.nqmgaming.shoesshop.model

import com.google.gson.annotations.SerializedName

data class Cart(
    val user: String, // Change this to String
    val items: ItemCart,
    val createdAt: String,
    val updatedAt: String,
    @SerializedName("_id")
    val id: String
)