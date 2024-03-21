package com.nqmgaming.shoesshop.model

import com.google.gson.annotations.SerializedName

data class ItemCart(
    val product: String,
    var quantity: Int
)
