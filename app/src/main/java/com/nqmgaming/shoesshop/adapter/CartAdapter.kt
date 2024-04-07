package com.nqmgaming.shoesshop.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.ItemCartBinding
import com.nqmgaming.shoesshop.model.Cart
import com.nqmgaming.shoesshop.model.Product
import com.nqmgaming.shoesshop.ui.activities.ProductDetailActivity
import retrofit2.Call

interface ProductCallback {
    fun onProductReceived(product: Product)
}

class CartAdapter(
    private val cartList: List<Cart>,
    private val onDeleted: (Cart) -> Unit,
    private val onUpdated: (Cart) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart, product: Product?) {
            if (product != null) {
                Glide.with(binding.root).load(product.image).into(binding.itemCartIv)
                binding.itemCartNameTv.text = product.name
                binding.itemCategoryTv.text = product.category.name
                binding.itemDescriptionTv.text = product.description

                val priceInMillions = (cart.items.quantity * product.price) / 1_000_000.0
                binding.itemCartPriceTv.text = String.format("%.2fM", priceInMillions)
                binding.itemCartQuantityTv.text = cart.items.quantity.toString()
                binding.itemSizeTv.text = "Size: ${cart.items.size}"
                binding.itemCartMinusIv.setOnClickListener {

                    if (cart.items.quantity > 1) {
                        cart.items.quantity -= 1
                        binding.itemCartQuantityTv.text = cart.items.quantity.toString()
                        val priceInMillions = (cart.items.quantity * product.price) / 1_000_000.0
                        binding.itemCartPriceTv.text = String.format("%.2fM", priceInMillions)
                        onUpdated(cart)
                    } else {
                        onDeleted(cart)
                        //remove item from cart
                        cartList.toMutableList().remove(cart)
                        notifyDataSetChanged()
                    }

                }

                binding.itemCartPlusIv.setOnClickListener {
                    if (cart.items.quantity < product.stock) {
                        cart.items.quantity += 1
                        binding.itemCartQuantityTv.text = cart.items.quantity.toString()
                        val priceInMillions = (cart.items.quantity * product.price) / 1_000_000.0
                        binding.itemCartPriceTv.text = String.format("%.2fM", priceInMillions)
                        onUpdated(cart)
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Stock is not enough",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                binding.root.setOnClickListener {
                    Intent(binding.root.context, ProductDetailActivity::class.java).apply {
                        putExtra("productId", product.id)
                        binding.root.context.startActivity(this)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = cartList[position]
        if (cart.items.product.isNotEmpty()) {
            getProductDetail(cart.items.product, object : ProductCallback {
                override fun onProductReceived(product: Product) {
                    holder.bind(cart, product)
                }
            })
        } else {
            // Handle the case when the product is empty
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private fun getProductDetail(productId: String, callback: ProductCallback) {
        val call: Call<Product> = ApiService.apiService.getProductById(productId)
        call.enqueue(object : retrofit2.Callback<Product> {
            override fun onResponse(call: Call<Product>, response: retrofit2.Response<Product>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        callback.onProductReceived(product)
                    } else {
                        // Handle the case when the product is null
                    }
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                // Handle failure here
            }
        })
    }
}