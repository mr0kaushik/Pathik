package com.pathik.ride.ui.activities.profile

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.pathik.ride.R
import com.pathik.ride.databinding.ActivityProfileBinding
import com.pathik.ride.databinding.AppBarLayoutBinding
import com.pathik.ride.model.Gender
import com.pathik.ride.model.User
import com.pathik.ride.network.Resource
import com.pathik.ride.utils.*
import com.squareup.picasso.Picasso
import com.vmadalin.easypermissions.EasyPermissions
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

enum class DialogType {
    CAMERA_PERMISSION_RATIONALE,
//    GALLERY_PERMISSION_RATIONALE
}

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var takeImage: ActivityResultLauncher<Uri>
    private lateinit var binding: ActivityProfileBinding
    private lateinit var btnDone: AppCompatImageButton
    private val viewModel by viewModels<ProfileViewModel>()

    private lateinit var user: User
    private var destinationUri: Uri? = null

    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appBarLayoutBinding = AppBarLayoutBinding.bind(binding.root.getChildAt(0))
        appBarLayoutBinding.tvAppBarTitle.text = getString(R.string.profile)
        appBarLayoutBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        takeImage =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
                if (success) {
                    cropImage(destinationUri!!)
                }
            }



        btnDone = appBarLayoutBinding.btnDone
        btnDone.apply {
            visible(true)
            setOnClickListener {
                hideKeyboard()
                val data = validateAllFields()
                data?.let {
                    saveData(data)
                }
            }
        }

        binding.profileContent.visible(false)
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshList(true)
        }


        binding.btnCamera.setOnClickListener {
            showImagePickerOptions()
        }

        binding.etName.addTextChangedListener(textWatcher)
        binding.etPhone.addTextChangedListener(textWatcher)

        refreshList(false)
    }

    private fun refreshList(fromSwipe: Boolean) {
        viewModel.fetchUserData().observe(this,
            {
                when (it) {
                    Resource.Loading -> {
                        if (!fromSwipe) binding.swipeRefreshLayout.isRefreshing = true
                    }
                    is Resource.Success -> {
                        user = it.value
                        setUserInfo(user)
                        binding.profileContent.visible(true)
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    is Resource.Failure -> {
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.root.snackbar(
                            it.exception?.message!!,
                            Snackbar.LENGTH_INDEFINITE,
                            getString(R.string.refresh)
                        ) {
                            binding.swipeRefreshLayout.isRefreshing = true
                        }

                    }
                }
            })
    }

    private fun setUserInfo(user: User) {
        binding.etName.setText(user.name)
        binding.etPhone.setText(user.phoneNumber)
        binding.tvEmail.text = user.email
        if (user.photoUrl.isNotEmpty()) {
            Picasso.get().load(user.photoUrl)
                .placeholder(R.drawable.ic_user_place_holder)
                .error(R.drawable.ic_user_place_holder)
                .into(binding.ivProfile)
        }
        binding.male.isChecked = user.gender == Gender.MALE
        binding.female.isChecked = user.gender == Gender.FEMALE
    }

    private fun getGender(): String {
        return if (binding.male.isChecked) Gender.MALE.name
        else Gender.FEMALE.name
    }


    private fun saveData(data: HashMap<String, Any>) {

        val bitmap =
            BitmapFactory.decodeFile(destinationUri?.path)

        viewModel.saveUserData(bitmap, data = data).observe(this) {
            when (it) {
                Resource.Loading -> {
                    progressDialog =
                        binding.root.context.getProgressDialog(R.string.updating_user_profile)
                            .show()
                }
                is Resource.Success -> {
                    lifecycleScope.launch {
                        setUserInfo(it.value)
                        progressDialog?.dismiss()
                        binding.root.snackbar("Profile updated successfully!!")
                    }
                }
                is Resource.Failure -> {
                    lifecycleScope.launch {
                        progressDialog?.dismiss()
                        if (it.exception is FirebaseException) {
                            binding.root.snackbar(getString(Util.getSimpleErrorResourceId(it.exception)))
                        } else {
                            binding.root.snackbar(getString(R.string.an_error_occur))
                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }


    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val name: String = binding.etName.text
                .toString()
                .trim()

            val phone: String = binding.etPhone.text
                .toString()
                .trim()

            resetErrors()
            btnDone.isEnabled = name.isNotEmpty()
                    && phone.isNotEmpty()
        }
    }

    private fun validateAllFields(): HashMap<String, Any>? {
        val name = binding.etName
            .text
            .toString()

        val phone = binding.etPhone
            .text
            .toString()

        if (!Patterns.PHONE.matcher(phone).matches() || phone.length != 10) {
            binding.tilPhoneNumber.error = "Enter valid phone number"
            return null
        }

        return hashMapOf(
            "name" to name,
            "phoneNumber" to phone,
            "gender" to getGender()
        )
    }

    private fun resetErrors() {
        binding.tilPhoneNumber.error = null
        binding.tilPhoneNumber.isErrorEnabled = false
        binding.tilName.error = null
        binding.tilName.isErrorEnabled = false
    }

    private fun showImagePickerOptions() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle(getString(R.string.lbl_set_profile_photo))
        val options = arrayOf(
            getString(R.string.lbl_take_camera_picture),
            getString(R.string.lbl_choose_from_gallery)
        )
        builder.setItems(
            options
        ) { _: DialogInterface?, which: Int ->
            when (which) {
                0 -> {
                    if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                        takePicture()
                    } else {
                        EasyPermissions.requestPermissions(
                            this,
                            PermissionUtil.getCameraPermissionRequest(this)
                        )
                    }
                }
                1 -> {
                    pickImages.launch("image/*")
                }
            }
        }
        builder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == PermissionUtil.REQUEST_CAMERA_CODE) {
            takePicture()
        }
//        } else if (requestCode == PermissionUtil.READ_EXTERNAL_STORAGE_CODE) {
//            pickImages.launch("image/*")
//        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (requestCode == PermissionUtil.REQUEST_CAMERA_CODE) {
            getDialog(DialogType.CAMERA_PERMISSION_RATIONALE)
        }
//        else if (requestCode == PermissionUtil.READ_EXTERNAL_STORAGE_CODE) {
//            getDialog(DialogType.GALLERY_PERMISSION_RATIONALE)
//        }
    }


    private fun takePicture() {
        destinationUri = Util.getImageDestinationPath(user.userId, this)
        takeImage.launch(destinationUri)
    }

    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { it ->
                cropImage(it)
            }
        }


    private fun cropImage(sourceUri: Uri) {
        destinationUri = sourceUri
        val dUri = Uri.fromFile(
            File(
                cacheDir, Util.queryName(
                    contentResolver, sourceUri
                )
            )
        )
        val options = UCrop.Options()
        options.setCompressionQuality(50)
        options.setToolbarColor(ContextCompat.getColor(this, R.color.bunker))
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.bunker))
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.white_smoke))
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.white_smoke))

        UCrop.of(sourceUri, dUri)
            .withOptions(options)
            .withAspectRatio(1f, 1f)
            .start(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == UCrop.REQUEST_CROP) {
                if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                    val resultUri: Uri? = UCrop.getOutput(data)
                    destinationUri = resultUri
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    val cropError: Throwable? = UCrop.getError(data)
                    Timber.e(cropError)
                    binding.root.snackbar("Image cropping failed !!")
                }
                setImageProfile()
            }
        }
    }

    private fun setImageProfile() {
        binding.ivProfile.setImageURI(destinationUri)
    }


    private fun getDialog(dialogType: DialogType) {
        val builder = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.permission_required))
            .setCancelable(true)
        when (dialogType) {
            DialogType.CAMERA_PERMISSION_RATIONALE -> {
                builder.setMessage(
                    getString(
                        R.string.configure_permission_in_app_settings,
                        "Camera"
                    )
                )
                    .setPositiveButton(R.string.open_settings) { dialog, _ ->
                        dialog.dismiss()
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", packageName, null)
                        })
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
//            DialogType.GALLERY_PERMISSION_RATIONALE -> {
//                builder.setMessage(
//                    getString(
//                        R.string.configure_permission_in_app_settings,
//                        "Storage"
//                    )
//                )
//                    .setPositiveButton(R.string.open_settings) { dialog, _ ->
//                        dialog.dismiss()
//                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//                            data = Uri.fromParts("package", packageName, null)
//                        })
//                    }
//                    .setNegativeButton(R.string.cancel) { dialog, _ ->
//                        dialog.dismiss()
//                    }
//                    .show()
//            }
        }
    }
}
