package com.nqmgaming.shoesshop

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.ActivitySigninBinding
import com.nqmgaming.shoesshop.model.SigninRequest
import com.nqmgaming.shoesshop.model.SigninResponse
import com.nqmgaming.shoesshop.util.JwtUtils
import retrofit2.Call

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        val view = binding.root
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = intent.getStringExtra("email")
        binding.emailTv.text = email

        binding.editTv.setOnClickListener {
            finish()
        }

        binding.signInBtn.setOnClickListener {
            val email = binding.emailTv.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()
            if (binding.passwordEt.text.toString().trim().isEmpty()) {
                binding.passwordEt.error = "Password is required"
                return@setOnClickListener
            }
            // check email and password
            checkLogin(email, password)
        }

    }

    private fun checkLogin(email: String, password: String) {

        val request = SigninRequest(email, password)
        val call: Call<SigninResponse> = ApiService.apiService.signin(request)
        call.enqueue(object : retrofit2.Callback<SigninResponse> {
            override fun onResponse(call: Call<SigninResponse>, response: retrofit2.Response<SigninResponse>) {
                if (response.isSuccessful) {
                    val accessToken = response.body()
                    if (accessToken != null) {
                        Log.d("SigninActivity", "onResponse: ${accessToken.accessToken}")
                        val userId = JwtUtils.decode(accessToken.accessToken, "nqmgaming")
                        Log.d("SigninActivity", "onResponse: $userId")
                    }
                }
            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                Log.d("SigninActivity", "onFailure: ${t.message}")
            }
        })

    }
}