package com.nqmgaming.shoesshop.model

data class Order(
    val id: String,
    val user: String,
    val items: List<ItemOrder>,
    val createAt: String,
    val updateAt: String
)
