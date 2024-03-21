package com.nqmgaming.shoesshop.ui.fragments

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
import com.nqmgaming.shoesshop.util.JwtUtils
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import retrofit2.Call

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var userId: String
    private lateinit var cartList: List<Cart>
    private lateinit var cartAdapter: CartAdapter

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
        userId = JwtUtils.decode(userId, "nqmgaming").toString()
        getCartByUserId(userId)
    }
    private fun getCartByUserId(userId: String) {
        val call: Call<List<Cart>> = ApiService.apiService.getCartByUserId(userId)
        call.enqueue(object : retrofit2.Callback<List<Cart>> {
            override fun onResponse(call: Call<List<Cart>>, response: retrofit2.Response<List<Cart>>) {
                if (response.isSuccessful) {
                    cartList = response.body()!!
                    Log.e("CartFragment", "onResponse: ${cartList}")
                    cartAdapter = CartAdapter(cartList)
                    binding.cartRv.adapter = cartAdapter
                    val layoutManger = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.cartRv.layoutManager = layoutManger
                    cartAdapter.notifyDataSetChanged()
                } else {
                    Log.e("ProductDetailActivity", "onResponseError: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Cart>>, t: Throwable) {
                Log.e("ProductDetailActivity", "onFailure: ${t.message}")
            }
        })
    }

}