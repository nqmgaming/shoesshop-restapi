package com.nqmgaming.shoesshop.model.signin

import com.nqmgaming.shoesshop.model.User

data class SigninResponse(
    val accessToken: String,
    val user: User
)