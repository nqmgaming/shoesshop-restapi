package com.nqmgaming.shoesshop

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoesshop.databinding.ActivityAuthBinding
import com.nqmgaming.shoesshop.databinding.ActivityEmailVerificationBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail

class EmailVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailVerificationBinding
    private lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.continueBtn.setOnClickListener{
            email = binding.emailEt.text.toString().trim()
            email.validEmail(){
                if (it.isEmpty()) {
                    binding.emailEt.error = null
                }else{
                    binding.emailEt.error = it
                }
            }

        }
    }
}