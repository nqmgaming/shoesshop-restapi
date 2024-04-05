package com.nqmgaming.shoesshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nqmgaming.shoesshop.databinding.ItemSumOrderBinding
import com.nqmgaming.shoesshop.model.OrderRequest

class SumOrderAdapter(
    private val sumOrderList: List<OrderRequest>
) : RecyclerView.Adapter<SumOrderAdapter.SumOrderViewHolder>() {

    inner class SumOrderViewHolder(private val binding: ItemSumOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            order: OrderRequest
        ) {
            binding.tvDay.text = order.time
            binding.tvTotalPrice.text = order.total.toString()
            binding.itemOrderRecyclerView.setHasFixedSize(true)
            binding.itemOrderRecyclerView.adapter = ItemOrderAdapter(order.products)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SumOrderViewHolder {
        val binding =
            ItemSumOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SumOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return sumOrderList.size
    }

    override fun onBindViewHolder(holder: SumOrderViewHolder, position: Int) {
        holder.bind(sumOrderList[position])
    }

}