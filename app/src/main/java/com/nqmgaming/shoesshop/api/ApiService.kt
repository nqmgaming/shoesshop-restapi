package com.nqmgaming.shoesshop.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nqmgaming.shoesshop.model.Cart
import com.nqmgaming.shoesshop.model.CartRequest
import com.nqmgaming.shoesshop.model.Category
import com.nqmgaming.shoesshop.model.ItemCart
import com.nqmgaming.shoesshop.model.Order
import com.nqmgaming.shoesshop.model.OrderRequest
import com.nqmgaming.shoesshop.model.Product
import com.nqmgaming.shoesshop.model.ProductStockResponse
import com.nqmgaming.shoesshop.model.StockUpdateRequest
import com.nqmgaming.shoesshop.model.signin.SigninRequest
import com.nqmgaming.shoesshop.model.signin.SigninResponse
import com.nqmgaming.shoesshop.model.signup.SignupRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    /**
     * Check server status
     * return true if the server is running else return false
     */

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

    /**
     * Patch the user by id
     * @param id the id of the user
     *
     */
    @Multipart
    @PATCH("api/v1/user/update/{id}")
    fun updateAvatar(@Path("id") id: String, @Part avatar: MultipartBody.Part): Call<SigninResponse>

    /**
     * Get category by id
     * @param id the id of the category
     * return the category if the category is exist in the database else return null
     */
    @GET("api/v1/category/{id}")
    fun getCategoryById(@Path("id") id: Int): Call<Category>

    /**
     * Get all products
     * return the list of products if the products are exist in the database else return null
     */
    @GET("api/v1/product/get-all")
    fun getAllProducts(@Query("sortOrder") sort: String): Call<ArrayList<Product>>

    /**
     * Search products by name
     * @param name the name of the product
     * return the list of products if the products are exist in the database else return null
     */
    @GET("api/v1/product/search")
    fun searchProducts(@Query("name") name: String): Call<ArrayList<Product>>

    /**
     * Get product by id
     * @param id the id of the product
     * return the product if the product is exist in the database else return null
     */
    @GET("api/v1/product/get/{id}")
    fun getProductById(@Path("id") id: String): Call<Product>

    /**
     * Create a new cart
     * @param userId the id of the user
     * @param productId the id of the product
     * @param quantity the quantity of the product
     */
    @POST("api/v1/cart/create")
    fun createCart(@Body request: CartRequest): Call<Cart>

    /**
     * Get all carts by user id
     * @param userId the id of the user
     * return the list of carts if the carts are exist in the database else return null
     */
    @GET("api/v1/cart/get-by-user/{id}")
    fun getCartByUserId(@Path("id") id: String): Call<List<Cart>>

    /**
     * Delete the cart by id
     * @param id the id of the cart
     */
    @DELETE("api/v1/cart/delete/{id}")
    fun deleteCart(@Path("id") id: String): Call<Cart>

    /**
     * PATCH the cart by id
     * @param id the id of the cart
     * @param items the list of items in the cart
     */

    @PATCH("api/v1/cart/update-quantity/{id}")
    fun updateCart(@Path("id") id: String, @Body items: CartRequest): Call<Cart>

    /**
     * Get all orders by userId
     * @param userId the id of the user
     * return the list of orders if the orders are exist in the database else return null
     */
    @GET("api/v1/order/get-by-user/{userId}")
    fun getOrdersByUserId(@Path("userId") userId: String): Call<List<OrderRequest>>

    /**
     * Create a new order
     * @param userId the id of the user
     * @param products the list of products in the order
     * @param total the total price of the order
     * @param address the address of the user
     * @param phoneNumber the phone number of the user
     * @param email the email of the user
     * @param time the time of the order
     */
    @POST("api/v1/order")
    fun postOrder(@Body request: OrderRequest): Call<Order>

    /**
     * Update stock of the product
     * @param id the id of the product
     * @param quantity the quantity of the product
     * return the product if the product is exist in the database else return null
     */
    @PATCH("api/v1/product/update-quantity/{id}")
    fun updateProductQuantity(
        @Path("id") id: String,
        @Body request: StockUpdateRequest
    ): Call<Product>

    /**
     * Get product stock by id
     * @param id the id of the product
     * return the stock of the product if the product is exist in the database else return null
     */
    @GET("api/v1/product/get-stock/{id}")
    fun getProductStock(@Path("id") id: String): Call<ProductStockResponse>

    companion object {
        private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val apiService: ApiService = Retrofit.Builder()
            .baseUrl("http://192.168.54.101:3001/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}