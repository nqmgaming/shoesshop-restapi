package com.nqmgaming.shoesshop.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.ItemOrderBinding
import com.nqmgaming.shoesshop.model.ItemOrder
import com.nqmgaming.shoesshop.model.Product
import com.nqmgaming.shoesshop.model.ProductOrderRequest
import retrofit2.Call
import retrofit2.Callback
import java.text.NumberFormat
import java.util.Locale

class ItemOrderAdapter(
    private val listOrder: List<ProductOrderRequest>
) : RecyclerView.Adapter<ItemOrderAdapter.ItemOrderViewHolder>() {
    inner class ItemOrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            product: Product,
            quantity: Int
        ) {
            binding.tvQuantityItemOrder.text = "Quantity: $quantity"
            Glide.with(binding.root.context)
                .load(product.image)
                .into(binding.ivItemOrder)
            binding.tvNameItemOrder.text = product.name
            binding.tvCategoryItemOrder.text = product.category.name
            val formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(product.price.toInt())
            binding.tvPriceItemOrder.text = "Price: ${formattedPrice}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemOrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: ItemOrderViewHolder, position: Int) {
        val productId = listOrder[position].product
        getProductById(productId, holder, listOrder[position].quantity)
    }

    private fun getProductById(productId: String, holder: ItemOrderViewHolder, quantity: Int) {
        val call: Call<Product> = ApiService.apiService.getProductById(productId)
        call.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: retrofit2.Response<Product>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        holder.bind(product, quantity)
                    }
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.e("ItemOrderAdapter", "onFailure: ${t.message}")
            }
        })
    }
}