package com.nqmgaming.shoesshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.databinding.ActivityCheckoutBinding
import com.nqmgaming.shoesshop.databinding.DialogChangeInfoBinding
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        val totalPrice = intent.getStringExtra("totalPrice")
        val totalQuantity = intent.getStringExtra("totalQuantity")
        val email = SharedPrefUtils.getString(this, "email")
        val address = SharedPrefUtils.getString(this, "address")
        val phoneNumber = SharedPrefUtils.getString(this, "phoneNumber")

        binding.emailTv.text = email
        binding.addressTv.text = address
        binding.phoneTv.text = phoneNumber

        binding.tvTotalValue.text = totalPrice + " VND"
        binding.tvShippingFeeValue.text = "Free"
        binding.tvTotalQuantityValue.text = totalQuantity

        binding.editAddressIv.setOnClickListener {
            val address = binding.addressTv.text.toString()
            editInformationDialog("Address", address)
        }

        binding.editPhoneIv.setOnClickListener {
            val phone = binding.phoneTv.text.toString()
            editInformationDialog("Phone", phone)
        }

        binding.editEmailIv.setOnClickListener {
            val email = binding.emailTv.text.toString()
            editInformationDialog("Email", email)
        }

        binding.btnCheckout.setOnClickListener {
            val address = binding.addressTv.text.toString()
            val phone = binding.phoneTv.text.toString()
            val email = binding.emailTv.text.toString()
            Log.d("CheckoutActivity", "onCreate: $address $phone $email")

            Intent(this, OrderActivity::class.java).apply {
                putExtra("address", address)
                putExtra("phone", phone)
                putExtra("email", email)
                putExtra("totalPrice", totalPrice)
                putExtra("totalQuantity", totalQuantity)
                startActivity(this)
            }
        }

    }

    private fun editInformationDialog(text: String, value: String = "") {

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Edit Information")
        val bindingDialog = DialogChangeInfoBinding.inflate(layoutInflater)
        bindingDialog.edtText.hint = text
        bindingDialog.edtText.setText(value)
        dialog.setView(bindingDialog.root)
        dialog.setMessage(text)
        dialog.setCancelable(false)
        dialog.setPositiveButton("OK", null)
        val alertDialog = dialog.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val value = bindingDialog.edtText.text.toString()
            var error = false
            if (text == "Address") {
                value.validator()
                    .nonEmpty()
                    .minLength(10)
                    .addErrorCallback {
                        bindingDialog.edtText.error = it
                        error = true
                    }.check()
                binding.addressTv.text = bindingDialog.edtText.text.toString()

            } else if (text == "Phone") {
                value.validator()
                    .nonEmpty()
                    .minLength(10)
                    .addErrorCallback {
                        bindingDialog.edtText.error = it
                        error = true
                    }.check()
                binding.phoneTv.text = bindingDialog.edtText.text.toString()

            } else {
                value.validator()
                    .nonEmpty()
                    .validEmail()
                    .addErrorCallback {
                        bindingDialog.edtText.error = it
                        error = true
                    }.check()
                binding.emailTv.text = bindingDialog.edtText.text.toString()

            }
            Log.d("CheckoutActivity", "editInformationDialog: $value $error")
            if (!error) {
                alertDialog.dismiss()
            }

        }

    }
}