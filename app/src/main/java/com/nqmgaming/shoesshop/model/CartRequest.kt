package com.nqmgaming.shoesshop.model

data class CartRequest(
    val user: String,
    val items: ItemCart,
    val createdAt: String,
    val updatedAt: String
)
data class StockUpdateRequest(val stock: Int)