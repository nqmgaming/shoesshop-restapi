package com.nqmgaming.shoesshop.model

data class CartRequest(
    val user: String,
    val items: List<ItemCart>,
    val createdAt: String,
    val updatedAt: String
)