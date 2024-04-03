package com.nqmgaming.shoesshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.databinding.ItemSizeBinding
import com.nqmgaming.shoesshop.model.Size

class SizeAdapter(
    private val sizeList: List<Size>,
    private val onSelectSize: (Size) -> Unit
) : RecyclerView.Adapter<SizeAdapter.SizeViewHolder>() {


    var selected = false

    inner class SizeViewHolder(private val binding: ItemSizeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(size: Size) {
            binding.tvSize.text = size.name
            binding.root.setOnClickListener {
                if (binding.root.tag == "none") {
                    notifyDataSetChanged()
                    binding.root.tag = "selected"
                    selected = true
                    onSelectSize(size)
                } else {
                    binding.root.tag = "none"
                    notifyDataSetChanged()
                    selected = false

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val binding = ItemSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return sizeList.size
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        holder.bind(sizeList[position])
    }
}