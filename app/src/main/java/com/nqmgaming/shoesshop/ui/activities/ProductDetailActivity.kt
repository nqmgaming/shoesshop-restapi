package com.nqmgaming.shoesshop.ui.activities

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.ActivityProductDetailBinding
import com.nqmgaming.shoesshop.model.Cart
import com.nqmgaming.shoesshop.model.CartRequest
import com.nqmgaming.shoesshop.model.ItemCart
import com.nqmgaming.shoesshop.model.Product
import com.nqmgaming.shoesshop.util.JwtUtils
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import retrofit2.Call
import java.util.Date

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var productId: String
    private lateinit var product: Product
    private lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener() {
            onBackPressedDispatcher.onBackPressed()
        }

        productId = intent.getStringExtra("productId").toString()
        getProductDetail(productId)
        userId = SharedPrefUtils.getString(this@ProductDetailActivity, "userId", "").toString()
        Log.e("HomeFragment", "onViewCreated: $userId")
        binding.addToBagBtn.setOnClickListener {
            addToBag(userId, productId)
        }

    }

    private fun addToBag(userId: String, productId: String) {
        val items = listOf(ItemCart(productId, 1))
        // Replace the JWT token with the actual user id
        val actualUserId = JwtUtils.decode(userId, "nqmgaming")
        val request = CartRequest(actualUserId!!, items, Date().toString(), Date().toString())
        val call: Call<Cart> = ApiService.apiService.createCart(request)
        call.enqueue(object : retrofit2.Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: retrofit2.Response<Cart>) {
                if (response.isSuccessful) {
                    val cart = response.body()!!
                    PopupDialog.getInstance(this@ProductDetailActivity)
                        .setStyle(Styles.SUCCESS)
                        .setHeading("Add to bag success!")
                        .setDescription("Continue purchase")
                        .setCancelable(false)
                        .showDialog(object : OnDialogButtonClickListener() {
                            override fun onDismissClicked(dialog: Dialog) {
                                super.onDismissClicked(dialog)
                                dialog.dismiss()
                            }
                        })
                    Log.e("ProductDetailActivity", "onResponse: ${response.body()}")
                } else {
                    response.errorBody()?.let {
                        Log.e("ProductDetailActivity", "onResponseError: ${it.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                Log.e("ProductDetailActivity", "onFailure: ${t.message}")
            }
        })
    }

    private fun getProductDetail(productId: String) {
        val call: Call<Product> = ApiService.apiService.getProductById(productId)
        call.enqueue(object : retrofit2.Callback<Product> {
            override fun onResponse(call: Call<Product>, response: retrofit2.Response<Product>) {
                if (response.isSuccessful) {
                    product = response.body()!!
                    Glide.with(this@ProductDetailActivity).load(product.image)
                        .into(binding.productImage)
                    binding.toolbar.title = product.name
                    binding.productName.text = product.name
                    binding.productPrice.text = "${product.price} đ"
                    binding.productCategory.text = product.category.name
                    binding.productDescription.text = product.description
                    binding.productStock.text = "Stock: ${product.stock}"
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


}