package com.nqmgaming.shoesshop.model.signup

data class SignupRequest(
    val email: String,
    val password: String,
    val image: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val address: String,
    val phoneNumber: String
)