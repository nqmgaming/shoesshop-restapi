package com.nqmgaming.shoesshop.model

data class ItemOrder(
    val id: Int,
    val product: Product,
    val quantity: Int,
    val price: Double
) {
}