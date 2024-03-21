package com.nqmgaming.shoesshop.model

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val birthDate: String,
    val image: String,
    val address: String,
    val phoneNumber: String,
    val createAt: String,
    val updateAt: String
)
