package com.nqmgaming.shoesshop.util

import com.auth0.android.jwt.JWT

object JwtUtils {
    fun decode(jwt: String, secretKey: String): String? {
        val jwtParser = JWT(jwt)
        val claim = jwtParser.getClaim("userId").asString()
        return claim
    }
}