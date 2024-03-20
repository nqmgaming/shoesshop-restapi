package com.nqmgaming.shoesshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.nqmgaming.shoesshop.databinding.ItemShoesBinding
import com.nqmgaming.shoesshop.model.Product
import java.util.ArrayList

internal class ShoesAdapter(
    private val shoesList: ArrayList<Product>,
    private val context: Context
) : BaseAdapter() {
    override fun getCount(): Int {
        return shoesList.size
    }

    override fun getItem(position: Int): Any {
        return shoesList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val product = this.shoesList[position]

        val binding = ItemShoesBinding.inflate(LayoutInflater.from(context), parent, false)

        binding.shoeName.text = product.name
        binding.shoePrice.text = product.price.toString()
        Glide.with(context).load(product.image).into(binding.shoeImage)

        return binding.root
    }
}
