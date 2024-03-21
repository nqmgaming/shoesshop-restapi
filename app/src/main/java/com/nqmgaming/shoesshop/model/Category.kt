package com.nqmgaming.shoesshop.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String,
    val numberOfProducts: String,
    val createdAt: String,
    val updatedAt: String
)
