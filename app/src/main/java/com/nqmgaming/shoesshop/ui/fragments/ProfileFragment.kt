package com.nqmgaming.shoesshop.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gun0912.tedpermission.coroutine.TedPermission
import com.nqmgaming.shoesshop.R
import com.nqmgaming.shoesshop.api.ApiService
import com.nqmgaming.shoesshop.databinding.FragmentProfileBinding
import com.nqmgaming.shoesshop.model.signin.SigninResponse
import com.nqmgaming.shoesshop.ui.activities.AuthActivity
import com.nqmgaming.shoesshop.ui.activities.SettingActivity
import com.nqmgaming.shoesshop.util.RealPathUtil
import com.nqmgaming.shoesshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.io.File
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private lateinit var userId: String

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


        userId = SharedPrefUtils.getString(requireContext(), "userId").toString()
        val firstName = SharedPrefUtils.getString(requireContext(), "firstName")
        val lastName = SharedPrefUtils.getString(requireContext(), "lastName")
        val avatar = SharedPrefUtils.getString(requireContext(), "avatar")
        binding.profileNameTv.text = "$firstName $lastName"
        if (avatar != null) {
            Glide.with(this).load(avatar).placeholder(R.drawable.ic_user_profile).into(binding.profileImageIv)
        }

        binding.profileImageIv.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                checkPermissionAndOpenCameraOrGallery()
            }
        }

        binding.logoutBtn.setOnClickListener {
            PopupDialog.getInstance(requireContext())
                .setStyle(Styles.STANDARD)
                .setHeading("Logout")
                .setDescription("Are you sure you want to logout?")
                .setPositiveButtonText("Yes")
                .setPositiveButtonBackground(R.color.black)
                .setNegativeButtonBackground(R.color.white)
                .setNegativeButtonText("No")
                .setCancelable(true)
                .showDialog(object : OnDialogButtonClickListener() {
                    override fun onNegativeClicked(dialog: Dialog?) {
                        super.onNegativeClicked(dialog)
                        dialog?.dismiss()
                    }

                    override fun onPositiveClicked(dialog: Dialog?) {
                        super.onPositiveClicked(dialog)
                        dialog?.dismiss()
                        SharedPrefUtils.clear(requireContext())
                        Intent(requireContext(), AuthActivity::class.java).apply {
                            startActivity(this)
                            requireActivity().finish()
                        }
                    }
                })
        }

    }

    private suspend fun checkPermissionAndOpenCameraOrGallery() {
        val permissionsResult = TedPermission.create()
            .setPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_MEDIA_IMAGES
            )
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .check()

        if (permissionsResult.isGranted) {
            withContext(Dispatchers.Main) {
                openCameraOrGallery()
            }
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }

    }

    private fun openCameraOrGallery() {
        ImagePicker.with(this)
            .crop()
            .cropSquare()
            .galleryMimeTypes(arrayOf("image/png", "image/jpeg"))
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                getResult.launch(intent)

            }
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "Image selected", Toast.LENGTH_SHORT).show()
                val uri = result.data?.data
                if (uri != null) {
                    imageUri = uri
                    Log.d("RegisterActivity", "Image uri: $uri")
                    Glide.with(this).load(uri).into(binding.profileImageIv)
                    uploadImage()

                } else {
                    Toast.makeText(requireContext(), "Image not found", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun uploadImage() {
        if (imageUri != null) {
            val realPath = RealPathUtil.getRealPath(requireContext(), imageUri!!)
            val avatarFile = realPath?.let { File(it) }
            if (avatarFile != null) {
                val requestFile = MultipartBody.Part.createFormData(
                    "avatar",
                    avatarFile.name,
                    avatarFile.asRequestBody("image/*".toMediaTypeOrNull())
                )

                val call = ApiService.apiService.updateAvatar(userId, requestFile)
                call.enqueue(object : retrofit2.Callback<SigninResponse> {
                    override fun onResponse(
                        call: Call<SigninResponse>,
                        response: retrofit2.Response<SigninResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                            SharedPrefUtils.saveString(
                                requireContext(),
                                "avatar",
                                response.body()?.user?.image.toString()
                            )
                        }
                    }

                    override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                        Log.e("ProfileFragment", "onFailure: ${t.message}")
                    }
                })
            }
        }
    }

}