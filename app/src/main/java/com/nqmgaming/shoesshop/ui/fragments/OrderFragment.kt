package com.nqmgaming.shoesshop.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.adapter.SumOrderAdapter
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.FragmentOrderBinding
import com.nqmgaming.shoesshop.model.Order
import com.nqmgaming.shoesshop.model.OrderRequest
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import retrofit2.Call
import retrofit2.Callback


class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var listOrder: List<OrderRequest>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = SharedPrefUtils.getString(requireContext(), "userId")

        getOrdersUser(userId)
    }

    private fun getOrdersUser(userId: String?) {
        if (userId != null) {
            val call = ApiService.apiService.getOrdersByUserId(userId)
            call.enqueue(object : Callback<List<OrderRequest>> {
                override fun onResponse(
                    call: Call<List<OrderRequest>>,
                    response: retrofit2.Response<List<OrderRequest>>
                ) {
                    if (response.isSuccessful) {
                       if (response.body()!!.isNotEmpty()){
                           listOrder = response.body()!!
                           binding.orderRecyclerView.setHasFixedSize(true)
                           binding.orderRecyclerView.adapter = SumOrderAdapter(listOrder)
                       }else{
                           binding.emptyOrderLayout.visibility = View.VISIBLE
                           binding.orderRecyclerView.visibility = View.GONE
                       }
                    }
                }

                override fun onFailure(call: Call<List<OrderRequest>>, t: Throwable) {
                    Log.e("OrderFragment", "onFailure: ${t.message}")
                }
            })
        }
    }
}