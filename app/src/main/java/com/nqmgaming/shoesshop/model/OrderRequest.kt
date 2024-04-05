package com.nqmgaming.shoesshop.model

import com.google.gson.annotations.SerializedName

data class ProductOrderRequest(
    @SerializedName("product") val product: String,
    @SerializedName("quantity") val quantity: Int
)

data class OrderRequest(
    @SerializedName("user") val user: String,
    @SerializedName("products") val products: List<ProductOrderRequest>,
    @SerializedName("total") val total: Int,
    @SerializedName("address") val address: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("email") val email: String,
    @SerializedName("time") val time: String
)

data class ProductStockResponse(
    @SerializedName("stock") val stock: Int
)