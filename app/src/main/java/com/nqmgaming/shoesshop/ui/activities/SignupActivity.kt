package com.nqmgaming.shoesshop.ui.activities

import android.app.DatePickerDialog
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
import com.nqmgaming.shoesshop.databinding.ActivitySignupBinding
import com.nqmgaming.shoesshop.model.signin.SigninResponse
import com.nqmgaming.shoesshop.model.signup.SignupRequest
import com.nqmgaming.shoesshop.util.JwtUtils
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import retrofit2.Call
import java.util.Calendar

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

        binding.dateTil.setOnClickListener {
            // Get the current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // This gets called when the user sets the date
                    // Update the date EditText with the selected date
                    val selectedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
                    binding.dateEt.setText(selectedDate)
                },
                year,
                month,
                day
            )

            // Show the dialog
            datePickerDialog.show()
        }

        binding.createAccountBtn.setOnClickListener {
            var error = false
            val email: String = binding.emailTv.text.toString().trim()
            val firstName: String = binding.firstnameEt.text.toString().trim()
            val lastName: String = binding.lastnameEt.text.toString().trim()
            val password: String = binding.passwordEt.text.toString().trim()
            val birthdate: String = binding.dateEt.text.toString().trim()
            val address: String = binding.addressEt.text.toString().trim()
            val phone: String = binding.phoneEt.text.toString().trim()

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

            address.validator()
                .nonEmpty()
                .minLength(10)
                .addErrorCallback {
                    binding.addressEt.error = it
                    error = true
                }.addSuccessCallback {
                    binding.addressEt.error = null
                }.check()

            phone.validator()
                .nonEmpty()
                .minLength(10)
                .addErrorCallback {
                    binding.phoneEt.error = it
                    error = true
                }.addSuccessCallback {
                    binding.phoneEt.error = null
                }.check()

            if (error) {
                return@setOnClickListener
            } else {
                val request =
                    SignupRequest(
                        email,
                        password,
                        "",
                        firstName,
                        lastName,
                        birthdate,
                        address,
                        phone
                    )
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
                        val userId = JwtUtils.decode(accessToken.accessToken, "nqmgaming")
                        SharedPrefUtils.saveBoolean(this@SignupActivity, "isLogin", true)
                        SharedPrefUtils.saveString(
                            this@SignupActivity,
                            "userId",
                            userId.toString()
                        )
                        SharedPrefUtils.saveString(
                            this@SignupActivity,
                            "email",
                            accessToken.user.email
                        )
                        SharedPrefUtils.saveString(
                            this@SignupActivity,
                            "phoneNumber",
                            accessToken.user.phoneNumber
                        )
                        SharedPrefUtils.saveString(
                            this@SignupActivity,
                            "address",
                            accessToken.user.address
                        )
                        PopupDialog.getInstance(this@SignupActivity)
                            .setStyle(Styles.SUCCESS)
                            .setHeading("Sign up success!")
                            .setDescription("Welcome to shoesshop")
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
