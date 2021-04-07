package com.pathik.ride.ui.activities.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pathik.ride.databinding.ActivityProfileBinding
import com.yalantis.ucrop.UCrop
import timber.log.Timber

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onStart() {
        super.onStart()
        Timber.i("On Start")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("ON Stop")
    }

    fun cropImage() {
//        UCrop.of(sourceUri, destinationUri)
//            .withAspectRatio(16, 9)
//            .withMaxResultSize(maxWidth, maxHeight)
//            .start(context);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                val resultUri: Uri? = UCrop.getOutput(data)
            } else if (resultCode == UCrop.RESULT_ERROR) {
                val cropError: Throwable? = UCrop.getError(data)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        Timber.i("on resume")
    }


    override fun onDestroy() {
        super.onDestroy()
        Timber.i("Ondestroy")
    }

//
//    private fun showImagePickerOptions() {
//        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.MAlertDialogStyle)
//        builder.setTitle(getString(R.string.lbl_set_profile_photo))
//        val options = arrayOf(
//            getString(R.string.lbl_take_camera_picture),
//            getString(R.string.lbl_choose_from_gallery)
//        )
//        builder.setItems(
//            options
//        ) { dialog: DialogInterface?, which: Int ->
//            when (which) {
//                0 -> if (ContextCompat.checkSelfPermission(
//                        this@EditProfile,
//                        Manifest.permission.CAMERA
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    launchCameraIntent()
//                } else {
//                    getRuntimePermission(com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_CAMERA)
//                }
//                1 -> if (ContextCompat.checkSelfPermission(
//                        this@EditProfile,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    launchGalleryIntent()
//                } else {
//                    getRuntimePermission(com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_READ_STORAGE)
//                }
//                2 -> onRemovePictureSelected()
//            }
//        }
//        builder.show()
//    }

//    private fun showImagePickerOptions() {
//        val builder = MaterialAlertDialogBuilder(this@EditProfile, R.style.MAlertDialogStyle)
//        builder.setTitle(getString(R.string.lbl_set_profile_photo))
//        val options = arrayOf(
//            getString(R.string.lbl_take_camera_picture),
//            getString(R.string.lbl_choose_from_gallery)
//        )
//        builder.setItems(
//            options
//        ) { dialog: DialogInterface?, which: Int ->
//            when (which) {
//                0 -> if (ContextCompat.checkSelfPermission(
//                        this@EditProfile,
//                        Manifest.permission.CAMERA
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    launchCameraIntent()
//                } else {
//                    getRuntimePermission(com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_CAMERA)
//                }
//                1 -> if (ContextCompat.checkSelfPermission(
//                        this@EditProfile,
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ) == PackageManager.PERMISSION_GRANTED
//                ) {
//                    launchGalleryIntent()
//                } else {
//                    getRuntimePermission(com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_READ_STORAGE)
//                }
//                2 -> onRemovePictureSelected()
//            }
//        }
//        builder.show()
//    }
//
//    private fun getRuntimePermission(type: Int) {
//        when (type) {
//            com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_CAMERA -> if (ContextCompat.checkSelfPermission(
//                    this@EditProfile,
//                    Manifest.permission.CAMERA
//                )
//                != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    this, arrayOf(Manifest.permission.CAMERA),
//                    com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_CAMERA
//                )
//            } else {
//                launchCameraIntent()
//            }
//            com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_READ_STORAGE -> if (ContextCompat.checkSelfPermission(
//                    this@EditProfile,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                )
//                != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                    com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_READ_STORAGE
//                )
//            } else {
//                launchGalleryIntent()
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_CAMERA -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                launchCameraIntent()
//            } else {
//                showSettingsDialog(com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_CAMERA)
//            }
//            com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_READ_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                launchGalleryIntent()
//            } else {
//                showSettingsDialog(com.tech.excessrewards.screens.EditProfile.PERMISSIONS_REQUEST_READ_STORAGE)
//            }
//            com.tech.excessrewards.screens.EditProfile.REQUEST_GALLERY_IMAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                launchGalleryIntent()
//            } else {
//                showSettingsDialog(com.tech.excessrewards.screens.EditProfile.REQUEST_GALLERY_IMAGE)
//            }
//        }
//    }
//
//
//    private fun launchCameraIntent() {
//        val bundle = Bundle()
//        bundle.putString(FAnalyticsConstant.Param.PROFILE_SELECTION_TYPE, "camera")
//        mAnalytics.logEvent(FAnalyticsConstant.Event.EDIT_SCREEN, bundle)
//        fileName = System.currentTimeMillis().toString() + ".jpg"
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        takePictureIntent.putExtra(
//            MediaStore.EXTRA_OUTPUT,
//            HelperClass.getCacheImagePath(this@EditProfile, fileName)
//        )
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(
//                takePictureIntent,
//                com.tech.excessrewards.screens.EditProfile.REQUEST_IMAGE_CAPTURE
//            )
//        }
//    }
//
//    private fun launchGalleryIntent() {
//        val bundle = Bundle()
//        bundle.putString(FAnalyticsConstant.Param.PROFILE_SELECTION_TYPE, "gallery")
//        mAnalytics.logEvent(FAnalyticsConstant.Event.EDIT_SCREEN, bundle)
//        val pickPhoto = Intent(
//            Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        )
//        startActivityForResult(
//            pickPhoto,
//            com.tech.excessrewards.screens.EditProfile.REQUEST_GALLERY_IMAGE
//        )
//    }
//
//
//    private fun cropImage(sourceUri: Uri?, type: Int) {
//        val destinationUri = Uri.fromFile(
//            File(
//                getCacheDir(),
//                HelperClass.queryName(getContentResolver(), sourceUri)
//            )
//        )
//        val options = UCrop.Options()
//        options.setCompressionQuality(80) //IMAGE_COMPRESSION = 80;
//        options.withAspectRatio(1f, 1f) //16:9, 3:4, 1:1
//        if (type == com.tech.excessrewards.screens.EditProfile.REQUEST_IMAGE_CAPTURE) {
//            options.withMaxResultSize(1000, 1000)
//        }
//        UCrop.of(sourceUri!!, destinationUri)
//            .withOptions(options)
//            .start(this)
//    }
//
//    private fun onRemovePictureSelected() {
//        //TODO REMOVE USER PROFILE PIC (PHASE 2)
//    }
//
//
//    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            com.tech.excessrewards.screens.EditProfile.REQUEST_CODE_VERIFY_EMAIL -> if (resultCode == RESULT_OK && data != null) {
//                val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
//                Log.d(
//                    com.tech.excessrewards.screens.EditProfile.TAG,
//                    "onActivityResult: Email ID : $accountName"
//                )
//                showSnackBar("$accountName: selected!", Snackbar.LENGTH_SHORT)
//            }
//            com.tech.excessrewards.screens.EditProfile.REQUEST_GALLERY_IMAGE -> if (resultCode == RESULT_OK && data != null) {
//                val imageUri = data.data
//                //                    ciProfileImage.setImageURI(imageUri);
//                cropImage(
//                    imageUri,
//                    com.tech.excessrewards.screens.EditProfile.REQUEST_GALLERY_IMAGE
//                )
//            }
//            com.tech.excessrewards.screens.EditProfile.REQUEST_IMAGE_CAPTURE -> if (resultCode == RESULT_OK) {
////                    ciProfileImage.setImageURI(HelperClass.getCacheImagePath(getApplicationContext(), fileName));
//                cropImage(
//                    HelperClass.getCacheImagePath(getApplicationContext(), fileName),
//                    com.tech.excessrewards.screens.EditProfile.REQUEST_IMAGE_CAPTURE
//                )
//            }
//            UCrop.REQUEST_CROP -> if (resultCode == RESULT_OK && data != null) {
//                val uri = UCrop.getOutput(data)
//                //                    ciProfileImage.setImageURI(uri);
//                if (uri != null && uri.path != null) {
//                    loadImg(uri)
//                }
//            }
//            UCrop.RESULT_ERROR -> if (data != null) {
//                val cropError = UCrop.getError(data)
//                Log.e(
//                    com.tech.excessrewards.screens.EditProfile.TAG,
//                    "Crop error: $cropError"
//                )
//            }
//        }
//
//        /*
//         * uncomment, Contact Permission Transaction
//         * Remove Comment to Use Functionality, also move the code before super()
//         */
//        /* comment start
//            callbackManager.onActivityResult(requestCode, resultCode, data);
//        comment end */
//    }


}