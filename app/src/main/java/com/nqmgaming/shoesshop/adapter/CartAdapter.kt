package com.nqmgaming.shoesshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.ItemCartBinding
import com.nqmgaming.shoesshop.model.Cart
import com.nqmgaming.shoesshop.model.Product
import retrofit2.Call

interface ProductCallback {
    fun onProductReceived(product: Product)
}

class CartAdapter(
    private val cartList: List<Cart>,
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cart: Cart, product: Product?) {
            if (product != null) {
                Glide.with(binding.root).load(product.image).into(binding.itemCartIv)
                binding.itemCartNameTv.text = product.name
                binding.itemCategoryTv.text = product.category.name
                binding.itemDescriptionTv.text = product.description

                binding.itemCartPriceTv.text = (cart.items[0].quantity * product.price).toString()
                binding.itemCartQuantityTv.text = cart.items[0].quantity.toString()
                binding.itemCartMinusIv.setOnClickListener {
                    if (cart.items[0].quantity > 1) {
                        cart.items[0].quantity -= 1
                        binding.itemCartQuantityTv.text = cart.items[0].quantity.toString()
                        binding.itemCartPriceTv.text =
                            (cart.items[0].quantity * product.price).toString()
                    }
                }

                binding.itemCartPlusIv.setOnClickListener {
                    if (cart.items[0].quantity < product.stock) {
                        cart.items[0].quantity += 1
                        binding.itemCartQuantityTv.text = cart.items[0].quantity.toString()
                        binding.itemCartPriceTv.text =
                            (cart.items[0].quantity * product.price).toString()
                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "Stock is not enough",
                            Toast.LENGTH_SHORT
                        ).show()
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
        if (cart.items.isNotEmpty()) {
            getProductDetail(cart.items[0].product, object : ProductCallback {
                override fun onProductReceived(product: Product) {
                    holder.bind(cart, product)
                }
            })
        } else {
            // Handle the case when the items list is empty
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