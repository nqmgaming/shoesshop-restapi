package com.nqmgaming.shoesshop.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.ActivitySigninBinding
import com.nqmgaming.shoesshop.model.signin.SigninRequest
import com.nqmgaming.shoesshop.model.signin.SigninResponse
import com.nqmgaming.shoesshop.util.JwtUtils
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
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
            override fun onResponse(
                call: Call<SigninResponse>,
                response: retrofit2.Response<SigninResponse>
            ) {
                if (response.isSuccessful) {
                    val accessToken = response.body()
                    if (accessToken != null) {
                        Log.d("SigninActivity", "onResponse: ${accessToken.accessToken}")
                        val userId = JwtUtils.decode(accessToken.accessToken, "nqmgaming")
                        Log.d("SigninActivity", "onResponse: $userId")
                        SharedPrefUtils.saveBoolean(this@SigninActivity, "isLogin", true)
                        if (userId != null) {
                            SharedPrefUtils.saveString(this@SigninActivity, "userId", userId)
                            SharedPrefUtils.saveString(
                                this@SigninActivity,
                                "email",
                                accessToken.user.email
                            )
                            SharedPrefUtils.saveString(
                                this@SigninActivity,
                                "phoneNumber",
                                accessToken.user.phoneNumber
                            )
                            SharedPrefUtils.saveString(
                                this@SigninActivity,
                                "address",
                                accessToken.user.address
                            )
                            SharedPrefUtils.saveString(
                                this@SigninActivity,
                                "firstName",
                                accessToken.user.firstName
                            )
                            SharedPrefUtils.saveString(
                                this@SigninActivity,
                                "lastName",
                                accessToken.user.lastName
                            )
                            SharedPrefUtils.saveString(
                                this@SigninActivity,
                                "birthdate",
                                accessToken.user.birthDate
                            )
                            SharedPrefUtils.saveString(
                                this@SigninActivity,
                                "avatar",
                                accessToken.user.image
                            )
                        }
                        PopupDialog.getInstance(this@SigninActivity)
                            .setStyle(Styles.SUCCESS)
                            .setHeading("Sign in success!")
                            .setDescription("Welcome back to shoesshop")
                            .setCancelable(false)
                            .showDialog(object : OnDialogButtonClickListener() {
                                override fun onDismissClicked(dialog: Dialog) {
                                    super.onDismissClicked(dialog)
                                    dialog.dismiss()
                                    intentToMain()
                                }
                            })

                    }
                }
            }

            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                Log.d("SigninActivity", "onFailure: ${t.message}")
            }
        })

    }

    private fun intentToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}