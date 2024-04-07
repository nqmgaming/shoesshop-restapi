package com.nqmgaming.shoesshop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.adapter.CartAdapter
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.FragmentCartBinding
import com.nqmgaming.shoesshop.model.Cart
import com.nqmgaming.shoesshop.model.CartRequest
import com.nqmgaming.shoesshop.ui.activities.CheckoutActivity
import com.nqmgaming.shoesshop.util.JwtUtils
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import retrofit2.Call
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var userId: String
    private lateinit var cartList: List<Cart>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var totalPrice: String
    private lateinit var totalQuantity: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = SharedPrefUtils.getString(requireContext(), "userId").toString()
        getCartByUserId(userId)

        binding.checkOutBtn.setOnClickListener{
            Intent(requireContext(), CheckoutActivity::class.java).apply {
                putExtra("totalPrice", totalPrice)
                putExtra("totalQuantity", totalQuantity)
                startActivity(this)

        }}

    }

    private fun getCartByUserId(userId: String) {
        val call: Call<List<Cart>> = ApiService.apiService.getCartByUserId(userId)
        call.enqueue(object : retrofit2.Callback<List<Cart>> {
            override fun onResponse(
                call: Call<List<Cart>>,
                response: retrofit2.Response<List<Cart>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.isEmpty()){
                        binding.emptyCartLayout.visibility = View.VISIBLE
                        binding.cartRv.visibility = View.GONE
                        binding.checkOutBtn.visibility = View.GONE
                        binding.totalItemsTv.visibility = View.GONE
                    }else{
                        cartList = response.body()!!
                        Log.e("CartFragment", "onResponse: ${cartList}")
                        cartAdapter = CartAdapter(cartList,
                            onDeleted = {
                                onDeleteCart(it.id)
                            },
                            onUpdated = {
                                onUpdateCart(it)
                            }
                        )
                        binding.cartRv.adapter = cartAdapter
                        val layoutManger =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        binding.cartRv.layoutManager = layoutManger
                        cartAdapter.notifyDataSetChanged()
                        calculateTotal(cartList)
                    }

                } else {
                    Log.e(
                        "ProductDetailActivity",
                        "onResponseError: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                Log.e("ProductDetailActivity", "onFailure: ${t.message}")
            }
        })
    }

    private fun onUpdateCart(it: Cart) {
        val items = it.items
        val cartRequest = CartRequest(userId, items,it.createdAt, it.updatedAt)
        val call: Call<Cart> = ApiService.apiService.updateCart(it.id, cartRequest)
        call.enqueue(object : retrofit2.Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: retrofit2.Response<Cart>) {
                if (response.isSuccessful) {
                    getCartByUserId(userId)
                } else {
                    Log.e("CartFragment", "onResponseError: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                Log.e("CartFragment", "onFailure: ${t.message}")
            }
        })

    }

    private fun onDeleteCart(cartId: String) {
        val call: Call<Cart> = ApiService.apiService.deleteCart(cartId)
        call.enqueue(object : retrofit2.Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: retrofit2.Response<Cart>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Cart deleted", Toast.LENGTH_SHORT).show()
                    getCartByUserId(userId)
                } else {
                    Log.e("CartFragment", "onResponseError: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                Log.e("CartFragment", "onFailure: ${t.message}")
            }
        })
    }

    private fun calculateTotal(cartList: List<Cart>) {
        var total = 0
        var quantity = 0
        for (cart in cartList) {
            total += cart.items.quantity * cart.items.price.toInt()
            quantity += cart.items.quantity
        }
        totalPrice = total.toString()
        totalQuantity = quantity.toString()
        val formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(totalPrice.toInt())
        binding.totalPriceTv.text = "Total: $formattedPrice VND"
        binding.totalItemsTv.text = "Total items: $totalQuantity"
    }

}