package com.nqmgaming.shoesshop.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nqmgaming.shoesshop.model.signin.SigninRequest
import com.nqmgaming.shoesshop.model.signin.SigninResponse
import com.nqmgaming.shoesshop.model.signup.SignupRequest
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    /**
     * Check if the user is not exist in the database by finding the user by email
     * @param email the email of the user
     * return true if the user is not exist in the database else return false
     */

    @GET("api/v1/user/check/{email}")
    fun checkUser(@Path("email") email: String): Call<Boolean>

    /**
     * Login the user by finding the user by email and password
     * @param email the email of the user
     * @param password the password of the user
     * return the jwt token if the user is exist in the database else return null
     */
    @POST("api/v1/user/signin")
    fun signin(@Body request: SigninRequest): Call<SigninResponse>

    /**
     * Signup the user by creating a new user
     * @param email the email of the user
     * @param password the password of the user
     * @param image the image of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param birthDate the birth date of the user
     * @param address the address of the user
     * @param phoneNumber the phone number of the user
     * return the jwt token if the user is exist in the database else return null
     */
    @POST("api/v1/user/signup")
    fun signup(@Body request: SignupRequest): Call<SigninResponse>

    companion object {
        private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val apiService: ApiService = Retrofit.Builder()
            .baseUrl("http://192.168.54.102:3001/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}