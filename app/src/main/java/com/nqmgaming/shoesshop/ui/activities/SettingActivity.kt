package com.nqmgaming.shoesshop.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.databinding.ActivitySettingBinding
import com.nqmgaming.shoesshop.util.SharedPrefUtils

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.logoutBtn.setOnClickListener {
            SharedPrefUtils.clear(this@SettingActivity)
            //finish all activities
            val intent = Intent(this@SettingActivity, AuthActivity::class.java)
            startActivity(intent)
            finishAffinity()

        }
    }
}