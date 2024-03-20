package com.nqmgaming.shoesshop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.ActivityAuthBinding
import com.nqmgaming.shoesshop.databinding.ActivityEmailVerificationBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import retrofit2.Call

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

        binding.continueBtn.setOnClickListener {
            email = binding.emailEt.text.toString().trim()
            email.validEmail() {
                if (it.isEmpty()) {
                    binding.emailEt.error = null
                } else {
                    binding.emailEt.error = it
                    return@validEmail

                }
            }
            Toast.makeText(this, "Email is valid", Toast.LENGTH_SHORT).show()
            checkUserIsExist(email)

        }
    }

    private fun checkUserIsExist(email: String) {
        val call: Call<Boolean> = ApiService.apiService.checkUser(email)
        call.enqueue(object : retrofit2.Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: retrofit2.Response<Boolean>) {
                if (response.isSuccessful) {
                    val isExist = response.body()
                    if (isExist == true) {
                        // intent to login with email
                        val intent =
                            Intent(this@EmailVerificationActivity, SigninActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                        Log.e("EmailVerify", "User is exist")
                    } else {
                        // intent to register with email
                        val intent =
                            Intent(this@EmailVerificationActivity, SignupActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                        Log.e("EmailVerify", "User is not exist")
                    }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("EmailVerify", "Error: ${t.message}")
            }
        })
    }
}