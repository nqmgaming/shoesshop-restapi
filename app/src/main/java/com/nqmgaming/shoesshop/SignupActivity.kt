package com.nqmgaming.shoesshop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.ActivitySignupBinding
import com.nqmgaming.shoesshop.model.signin.SigninResponse
import com.nqmgaming.shoesshop.model.signup.SignupRequest
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import com.wajahatkarim3.easyvalidation.core.view_ktx.minLength
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import retrofit2.Call

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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

        binding.createAccountBtn.setOnClickListener {
            var error = false
            val email: String = binding.emailTv.text.toString().trim()
            val firstName: String = binding.firstnameEt.text.toString().trim()
            val lastName: String = binding.lastnameEt.text.toString().trim()
            val password: String = binding.passwordEt.text.toString().trim()
            val birthdate: String = binding.dateEt.text.toString().trim()

            firstName.validator()
                .nonEmpty()
                .minLength(3)
                .addErrorCallback {
                    binding.firstnameEt.error = it
                    error = true
                }.addSuccessCallback {
                    binding.firstnameEt.error = null
                }.check()

            lastName.validator()
                .nonEmpty()
                .minLength(3)
                .addErrorCallback {
                    binding.lastnameEt.error = it
                    error = true
                }.addSuccessCallback {
                    binding.lastnameEt.error = null
                }.check()

            password.validator()
                .nonEmpty()
                .atleastOneUpperCase()
                .atleastOneLowerCase()
                .atleastOneNumber()
                .atleastOneSpecialCharacters()
                .minLength(8)
                .addErrorCallback {
                    binding.passwordEt.error = it
                    error = true
                }.addSuccessCallback {
                    binding.passwordEt.error = null
                }.check()

            birthdate.validator()
                .nonEmpty()
                .addErrorCallback {
                    binding.dateEt.error = it
                    error = true
                }.addSuccessCallback {
                    binding.dateEt.error = null
                }.check()

            if (error) {
                return@setOnClickListener
            } else {
                val request =
                    SignupRequest(email, password, "", firstName, lastName, birthdate, "", "")
                signup(request)
            }
        }

    }

    private fun signup(request: SignupRequest) {
        val call: Call<SigninResponse> = ApiService.apiService.signup(request)
        call.enqueue(object : retrofit2.Callback<SigninResponse> {
            override fun onResponse(
                call: Call<SigninResponse>,
                response: retrofit2.Response<SigninResponse>
            ) {
                if (response.isSuccessful) {
                    val accessToken = response.body()
                    if (accessToken != null) {
                        SharedPrefUtils.saveBoolean(this@SignupActivity, "isLogin", true)
                        SharedPrefUtils.saveString(
                            this@SignupActivity,
                            "userId",
                            accessToken.accessToken
                        )
                        intentToMain()
                    }
                }
            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                Log.d("SignupActivity", "onFailure: ${t.message}")
            }
        })

    }

    private fun intentToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
