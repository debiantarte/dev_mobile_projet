package com.example.dm_project

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.bumptech.glide.Glide
import com.example.dm_project.network.API
import com.example.dm_project.network.UserRepository
import com.example.dm_project.worker.FilterWorker
import com.example.dm_project.worker.KEY_IMAGE_URI
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class UserInfoActivity : AppCompatActivity() {
    companion object {
        const val CAMERA_PERMISSION_CODE = 1000
        const val CAMERA_REQUEST_CODE = 2001
        const val PICK_IMAGE = 1001
    }

    val userRepository = UserRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        take_picture_button.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }
        upload_image_button.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
        }
    }

    private fun askCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE )
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE )
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            openCamera()

        else
            Toast.makeText(this, "We need access to your camera to take a picture :'(", Toast.LENGTH_LONG).show()
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return

        val workManager = WorkManager.getInstance()

        if (data.data != null) {
            val applySepiaFilter = buildSepiaFilterRequest(data)
            handleWorkRequest(workManager, applySepiaFilter)
        }
        else
        {
            userRepository.getAvatarUri().observe(this, Observer {
                if (it != null)
                {
                    val applySepiaFilter = buildSepiaFilterRequest(it)
                    handleWorkRequest(workManager, applySepiaFilter)
                }
            })
        }
    }

    private fun handleWorkRequest(manager: WorkManager, applySepiaFilter : OneTimeWorkRequest) {
        manager.beginWith(applySepiaFilter).enqueue()

        manager.getWorkInfoByIdLiveData(applySepiaFilter.id)
            .observe(this, Observer { info ->
                if (info != null && info.state.isFinished) {
                    val sepiaFilteredImage = info.outputData.getByteArray("SEPIA_FILTERED_BYTEARRAY")
                    val sepiaImage = BitmapFactory.decodeByteArray(sepiaFilteredImage, 0, sepiaFilteredImage!!.size)
                    Glide.with(this).load(sepiaImage).fitCenter().circleCrop().into(current_avatar)
                    val imageBody = imageToBody(sepiaImage)
                    if (imageBody != null) {
                        MainScope().launch {
                            API.INSTANCE.userService.updateAvatar(imageBody)
                        }
                    }
                }
            })
    }

    private fun handlePictureChosen(data: Intent?): OneTimeWorkRequest? {
        if(data?.data == null) return null
        return buildSepiaFilterRequest(data)
    }

    private fun handlePhotoTaken(data: Intent?): OneTimeWorkRequest? {
        if (data?.data == null) return null
        val image = data.extras?.get("data") as? Bitmap
        val uri = userRepository.getAvatarUri().value
        if (uri == null) return null
        return buildSepiaFilterRequest(uri)
    }

    // Vous pouvez ignorer cette fonction...
    private fun imageToBody(image: Bitmap?): MultipartBody.Part? {
        val f = File(cacheDir, "tmpfile.jpg")
        f.createNewFile()
        try {
            val fos = FileOutputStream(f)
            image?.compress(Bitmap.CompressFormat.PNG, 100, fos)

            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()

        }

        val body = RequestBody.create(MediaType.parse("image/png"), f)
        return MultipartBody.Part.createFormData("avatar", f.path ,body)
    }

    private fun buildSepiaFilterRequest(intent: Intent): OneTimeWorkRequest {
        return OneTimeWorkRequest.Builder(FilterWorker::class.java)
                .setInputData(buildInputDataForFilter(intent.data))
                .build()
    }

    private fun buildSepiaFilterRequest(uri: Uri): OneTimeWorkRequest {
        return  OneTimeWorkRequest.Builder(FilterWorker::class.java)
            .setInputData(buildInputDataForFilter(uri))
            .build()
    }

    private fun buildInputDataForFilter(imageUri: Uri?): Data {
        val builder = Data.Builder()
        if (imageUri != null) {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }
}
