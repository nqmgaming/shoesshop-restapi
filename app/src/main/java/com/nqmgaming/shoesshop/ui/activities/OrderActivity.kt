package com.nqmgaming.shoesshop.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.ActivityOrderBinding
import com.nqmgaming.shoesshop.model.*
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import retrofit2.Call
import retrofit2.Callback
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private lateinit var cartList: List<Cart>
    private var cartItemOrderList: MutableList<ProductOrderRequest> = mutableListOf()
    private var cartItemOrderIdList: MutableList<String> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val totalPrice = intent.getStringExtra("totalPrice")
        val totalQuantity = intent.getStringExtra("totalQuantity")
        val email = intent.getStringExtra("email")
        val address = intent.getStringExtra("address")
        val phoneNumber = intent.getStringExtra("phone")
        val userId = SharedPrefUtils.getString(this, "userId")

        //get date
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(LocalDate.now().toString(), formatter)


        binding.btnNext.setOnClickListener {
            getAllCartItems(userId!!, address, phoneNumber, email, totalPrice, date)
        }

    }

    private fun getAllCartItems(
        userId: String,
        address: String?,
        phoneNumber: String?,
        email: String?,
        totalPrice: String?,
        date: LocalDate?
    ) {
        val call: Call<List<Cart>> = ApiService.apiService.getCartByUserId(userId)
        call.enqueue(object : Callback<List<Cart>> {
            override fun onResponse(
                call: Call<List<Cart>>,
                response: retrofit2.Response<List<Cart>>
            ) {
                if (response.isSuccessful) {
                    cartList = response.body()!!
                    for (cart in cartList) {
                        val cartItemOrder =
                            ProductOrderRequest(cart.items.product, cart.items.quantity)
                        cartItemOrderList += cartItemOrder
                        cartItemOrderIdList.add(cart.id)
                    }
                    createOrder(
                        userId,
                        cartItemOrderList,
                        address,
                        phoneNumber,
                        email,
                        totalPrice,
                        date
                    )

                    deleteCartItems(cartItemOrderIdList)
                }
            }

            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                Log.e("OrderActivity", "onFailure get all: ${t.message}")
            }
        })
    }

    private fun createOrder(
        userId: String,
        cartItemOrderList: List<ProductOrderRequest>,
        address: String?,
        phoneNumber: String?,
        email: String?,
        totalPrice: String?,
        date: LocalDate?
    ) {
        if (address != null && phoneNumber != null && email != null && totalPrice != null) {
            val orderRequest = OrderRequest(
                userId,
                cartItemOrderList,
                totalPrice.toInt(),
                address,
                phoneNumber,
                email,
                date.toString()
            )
            val call: Call<Order> = ApiService.apiService.postOrder(orderRequest)
            call.enqueue(object : Callback<Order> {
                override fun onResponse(call: Call<Order>, response: retrofit2.Response<Order>) {
                    if (response.isSuccessful) {
                        val order = response.body()
                        if (order != null) {

                            Toast.makeText(
                                this@OrderActivity,
                                "Order created successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            for (item in cartItemOrderList) {
                                val productId = item.product
                                getProductStock(productId) { currentStock ->
                                    val newStock = currentStock - item.quantity
                                    Log.d("OrderActivity", "onResponse Stock: $newStock")
                                    updateProductStock(productId, newStock)
                                }
                            }
                            PopupDialog.getInstance(this@OrderActivity)
                                .setStyle(Styles.SUCCESS)
                                .setHeading("Order created successfully!")
                                .setDescription("Thanks for your purchase")
                                .setCancelable(false)
                                .showDialog(object : OnDialogButtonClickListener() {
                                    override fun onDismissClicked(dialog: Dialog) {
                                        super.onDismissClicked(dialog)
                                        dialog.dismiss()
                                        Intent(this@OrderActivity, MainActivity::class.java).apply {
                                            startActivity(this)
                                            finish()
                                        }
                                    }
                                })

                        } else {

                            Toast.makeText(
                                this@OrderActivity,
                                "Order created failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<Order>, t: Throwable) {
                    Log.e("OrderActivity", "onFailure create: ${t.message}")
                }
            })
        } else {
            Toast.makeText(this, "Please fill all the information", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteCartItems(cartItemOrderIdList: List<String>) {
        for (cartItemId in cartItemOrderIdList) {
            val call: Call<Cart> = ApiService.apiService.deleteCart(cartItemId.toString())
            call.enqueue(object : Callback<Cart> {
                override fun onResponse(call: Call<Cart>, response: retrofit2.Response<Cart>) {
                    if (response.isSuccessful) {
                        Log.d("OrderActivity", "onResponse: Cart item deleted")
                    }
                }

                override fun onFailure(call: Call<Cart>, t: Throwable) {
                    Log.e("OrderActivity", "onFailure Delete: ${t.message}")
                }
            })
        }
    }

    private fun updateProductStock(productId: String, newStock: Int) {
        val stockUpdateRequest = StockUpdateRequest(newStock)
        val call: Call<Product> =
            ApiService.apiService.updateProductQuantity(productId, stockUpdateRequest)
        call.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: retrofit2.Response<Product>) {
                if (response.isSuccessful) {
                    Log.d("OrderActivity", "onResponse: Product stock updated")
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.e("OrderActivity", "onFailure update stock: ${t.message}")
            }
        })
    }

    private fun getProductStock(productId: String, callback: (Int) -> Unit) {
        val call: Call<ProductStockResponse> = ApiService.apiService.getProductStock(productId)
        call.enqueue(object : Callback<ProductStockResponse> {
            override fun onResponse(
                call: Call<ProductStockResponse>,
                response: retrofit2.Response<ProductStockResponse>
            ) {
                if (response.isSuccessful) {
                    val productStockResponse = response.body()
                    if (productStockResponse != null) {
                        callback(productStockResponse.stock)
                    }
                }
            }

            override fun onFailure(call: Call<ProductStockResponse>, t: Throwable) {
                Log.e("OrderActivity", "onFailure get product stock: ${t.message}")
            }
        })
    }
}