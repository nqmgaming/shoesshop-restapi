package com.nqmgaming.shoesshop.model

data class Cart(
    val id: String,
    val user: User,
    val items: List<ItemCart>,
    val createAt: String,
    val updateAt: String
)
