package com.nqmgaming.shoesshop.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.ui.activities.ProductDetailActivity
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
        binding.shoePrice.text = product.price.toString() + " đ"
        Glide.with(context).load(product.image).into(binding.shoeImage)
        binding.shoeCategory.text = product.category.name
        binding.heartImage.setOnClickListener {
            if (binding.heartImage.tag == "empty") {
                binding.heartImage.setImageResource(R.drawable.ic_heart_red)
                binding.heartImage.tag = "filled"
            } else {
                binding.heartImage.setImageResource(R.drawable.ic_heart_border)
                binding.heartImage.tag = "empty"
            }
        }

        return binding.root
    }
}
