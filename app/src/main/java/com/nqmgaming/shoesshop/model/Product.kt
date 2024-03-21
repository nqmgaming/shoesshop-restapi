package com.nqmgaming.shoesshop.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: Category,
    val image: String,
    val stock: Int,
    val createdAt: String,
    val updatedAt: String
)
