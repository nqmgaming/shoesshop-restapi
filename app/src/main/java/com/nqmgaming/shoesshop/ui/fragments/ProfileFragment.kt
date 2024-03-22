package com.nqmgaming.shoesshop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.databinding.FragmentProfileBinding
import com.nqmgaming.shoesshop.ui.activities.SettingActivity


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsContainer.setOnClickListener{
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }
    }


}