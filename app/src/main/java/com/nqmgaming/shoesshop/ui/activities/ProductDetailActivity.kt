package com.nqmgaming.shoesshop.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.ActivityProductDetailBinding
import com.nqmgaming.shoesshop.model.Product
import retrofit2.Call

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var productId: String
    private lateinit var product: Product
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
                    binding.productPrice.text = product.price.toString() + " đ"
                    binding.productCategory.text = product.category.name
                    binding.productDescription.text = product.description
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