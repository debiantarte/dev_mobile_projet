package com.example.dm_project.worker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.effect.EffectFactory
import android.net.Uri
import android.preference.PreferenceManager
import android.provider.MediaStore
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.example.dm_project.network.API
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

const val KEY_IMAGE_URI = "IMAGE_URI"

class FilterWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result = try {
        val imageUriString = inputData.getString(KEY_IMAGE_URI)

        val bitmap = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, Uri.parse(imageUriString))

        if (bitmap == null) throw Exception("No avatar found!")
        val filteredBitmap = toSepia(bitmap)

        val compressedBmp = getResizedBitmap(filteredBitmap, 150)

        /* Bitmap -> Compressed ByteArray */
        val baos = ByteArrayOutputStream()
        compressedBmp.compress(Bitmap.CompressFormat.JPEG, 10, baos)

        val byteArray = baos.toByteArray()

        println(byteArray.size)

        val output = Data.Builder().putByteArray("SEPIA_FILTERED_BYTEARRAY", byteArray).build()

        Result.success(output)
    }
    catch (e: Throwable) {
        println(e.message)
        Result.failure()
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun toSepia(original: Bitmap): Bitmap {
        val height = original.height
        val width = original.width

        val sepia = original.copy(Bitmap.Config.RGB_565, true)

        for (i in 0 until width){
            for (j in 0 until height){
                val oldPixel = original.getPixel(i, j)
                val oldRed = Color.red(oldPixel)
                val oldGreen = Color.green(oldPixel)
                val oldBlue = Color.blue(oldPixel)

                val newRed = 0.393f * oldRed + 0.769f * oldGreen + 0.189f * oldBlue
                val newGreen = 0.349f * oldRed + 0.686f * oldGreen + 0.168f * oldBlue
                val newBlue = 0.272f * oldRed + 0.534f * oldGreen + 0.131f * oldBlue

                val newPixel = Color.rgb(newRed, newGreen, newBlue)
                sepia.setPixel(i, j, newPixel)
            }
        }

        return sepia
    }
}