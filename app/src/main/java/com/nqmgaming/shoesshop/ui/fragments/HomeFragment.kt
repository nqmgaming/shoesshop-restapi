package com.nqmgaming.shoesshop.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.adapter.ShoesAdapter
import com.nqmgaming.shoesshop.databinding.FragmentHomeBinding
import com.nqmgaming.shoesshop.model.Category
import com.nqmgaming.shoesshop.model.Product


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var shoesAdapter: ShoesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //create category list
        val categoryList = ArrayList<Category>()
        categoryList.add(Category(1, "Men", "Men's shoes", 5, "2022-01-01", "2022-01-01"))
        categoryList.add(Category(2, "Women", "Women's shoes", 5, "2022-01-01", "2022-01-01"))
        categoryList.add(Category(3, "Kids", "Kids' shoes", 5, "2022-01-01", "2022-01-01"))

        val shoesList = ArrayList<Product>()
        shoesList.add(
            Product(
                1,
                "Air Jordan 1",
                "Description 1",
                5279000.0,
                categoryList[1],
                "https://static.nike.com/a/images/c_limit,w_592,f_auto/t_product_v1/e777c881-5b62-4250-92a6-362967f54cca/air-force-1-07-shoes-NMmm1B.png",
                10,
                "2022-01-01",
                "2022-01-01"
            )
        )
        shoesList.add(
            Product(
                2,
                "Air Jordan 2",
                "Description 2",
                5279000.0,
                categoryList[1],
                "https://static.nike.com/a/images/c_limit,w_592,f_auto/t_product_v1/e777c881-5b62-4250-92a6-362967f54cca/air-force-1-07-shoes-NMmm1B.png",
                10,
                "2022-01-01",
                "2022-01-01"
            )
        )
        shoesList.add(
            Product(
                3,
                "Air Jordan 3",
                "Description 3",
                5279000.0,
                categoryList[1],
                "https://static.nike.com/a/images/c_limit,w_592,f_auto/t_product_v1/e777c881-5b62-4250-92a6-362967f54cca/air-force-1-07-shoes-NMmm1B.png",
                10,
                "2022-01-01",
                "2022-01-01"
            )
        )
        shoesList.add(
            Product(
                4,
                "Air Jordan 4",
                "Description 4",
                5279000.0,
                categoryList[1],
                "https://static.nike.com/a/images/c_limit,w_592,f_auto/t_product_v1/e777c881-5b62-4250-92a6-362967f54cca/air-force-1-07-shoes-NMmm1B.png",
                10,
                "2022-01-01",
                "2022-01-01"
            )
        )
        shoesList.add(
            Product(
                5,
                "Air Jordan 5",
                "Description 5",
                5279000.0,
                categoryList[1],
                "https://static.nike.com/a/images/c_limit,w_592,f_auto/t_product_v1/e777c881-5b62-4250-92a6-362967f54cca/air-force-1-07-shoes-NMmm1B.png",
                10,
                "2022-01-01",
                "2022-01-01"
            )
        )

        shoesAdapter = ShoesAdapter(shoesList, requireContext())
        binding.shoesGrid.adapter = shoesAdapter
        binding.shoesGrid.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                Toast.makeText(
                    requireContext(),
                    "You clicked on ${shoesList[position].name}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}