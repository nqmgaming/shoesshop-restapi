package com.nqmgaming.shoesshop.model

data class Order(
    val id: Int,
    val user: User,
    val items: List<ItemOrder>,
    val createAt: String,
    val updateAt: String
)
