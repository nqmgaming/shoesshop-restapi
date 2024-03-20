package com.nqmgaming.shoesshop.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: Category,
    val image: String,
    val stock: Int,
    val createAt: String,
    val updateAt: String
)
