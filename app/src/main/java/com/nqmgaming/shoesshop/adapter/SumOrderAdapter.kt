package com.nqmgaming.shoesshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nqmgaming.shoesshop.databinding.ItemSumOrderBinding
import com.nqmgaming.shoesshop.model.OrderRequest
import java.text.SimpleDateFormat
import java.util.Locale

class SumOrderAdapter(
    private val sumOrderList: List<OrderRequest>
) : RecyclerView.Adapter<SumOrderAdapter.SumOrderViewHolder>() {

    inner class SumOrderViewHolder(private val binding: ItemSumOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            order: OrderRequest
        ) {
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val targetFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = originalFormat.parse(order.time)
            val formattedDate = if (date != null) targetFormat.format(date) else ""
            binding.tvDay.text = formattedDate
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