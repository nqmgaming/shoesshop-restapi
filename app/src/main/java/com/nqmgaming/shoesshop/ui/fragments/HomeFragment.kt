package com.nqmgaming.shoesshop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.adapter.ShoesAdapter
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.FragmentHomeBinding
import com.nqmgaming.shoesshop.model.Category
import com.nqmgaming.shoesshop.model.Product
import com.nqmgaming.shoesshop.ui.activities.ProductDetailActivity
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import retrofit2.Call


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var shoesAdapter: ShoesAdapter
    private lateinit var shoesList: ArrayList<Product>
    private lateinit var userId: String
    private var sort: String = "asc"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = "Home"
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        getAllProducts()
        binding.swipeRefreshLayout.setOnRefreshListener {
            getAllProducts()
            binding.swipeRefreshLayout.isRefreshing = false
        }


    }

    private fun getAllProducts() {
        val call: Call<ArrayList<Product>> = ApiService.apiService.getAllProducts(sort)
        call.enqueue(object : retrofit2.Callback<ArrayList<Product>> {
            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: retrofit2.Response<ArrayList<Product>>
            ) {
                if (response.isSuccessful) {
                    shoesList = response.body()!!
                    Log.e("HomeFragment", "onResponse: ${shoesList}")
                    shoesAdapter = ShoesAdapter(shoesList, requireContext())
                    binding.shoesGrid.adapter = shoesAdapter
                    binding.shoesGrid.onItemClickListener =
                        AdapterView.OnItemClickListener { parent, view, position, id ->
                            val product = shoesList[position]
                            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
                            intent.putExtra("productId", product.id)
                            startActivity(intent)
                        }

                } else {
                    Log.e("HomeFragment", "onResponse: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Log.e("HomeFragment", "onFailure: ${t.message}")
            }
        })

    }

    private fun searchByName(name:String) {
        val call: Call<ArrayList<Product>> = ApiService.apiService.searchProducts(name)
        call.enqueue(object : retrofit2.Callback<ArrayList<Product>> {
            override fun onResponse(
                call: Call<ArrayList<Product>>,
                response: retrofit2.Response<ArrayList<Product>>
            ) {
                if (response.isSuccessful) {
                    shoesList = response.body()!!
                    Log.e("HomeFragment", "onResponse: ${shoesList}")
                    shoesAdapter = ShoesAdapter(shoesList, requireContext())
                    binding.shoesGrid.adapter = shoesAdapter
                    binding.shoesGrid.onItemClickListener =
                        AdapterView.OnItemClickListener { parent, view, position, id ->
                            val product = shoesList[position]
                            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
                            intent.putExtra("productId", product.id)
                            startActivity(intent)
                        }

                } else {
                    Log.e("HomeFragment", "onResponse: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<Product>>, t: Throwable) {
                Log.e("HomeFragment", "onFailure: ${t.message}")
            }
        })
    }

    @Deprecated("This method is deprecated")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        when (menu) {
            else -> {
                val item = menu.findItem(R.id.action_search)
                val searchView = item.actionView as androidx.appcompat.widget.SearchView
                searchView.queryHint = "Search"
                searchView.setOnQueryTextListener(object :
                    androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        searchByName(query!!)
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        searchByName(newText!!)
                        return false
                    }
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                // Handle search action
                true
            }

            R.id.sort_increasing -> {
                sort = "asc"
                getAllProducts()
                true
            }

            R.id.sort_decreasing -> {
                sort = "desc"
                getAllProducts()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}