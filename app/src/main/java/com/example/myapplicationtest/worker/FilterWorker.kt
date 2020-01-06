package com.example.myapplicationtest.worker

import android.content.Context
import android.graphics.Bitmap
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.example.dm_project.network.API
import com.example.dm_project.network.App
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

const val KEY_IMAGE_URI = "IMAGE_URI"

class FilterWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result = try {
        val imageUriString = inputData.getString(KEY_IMAGE_URI)
        val bitmap = Glide.with(this.context).load(imageUriString) as? Bitmap

        if (bitmap == null) throw Exception("No avatar found!")
        val filteredBitmap = toSepia(bitmap!!)

        //write filteredBitmap on a file and output uri

        Result.success()
    }
    catch (e: Throwable) {
        println(e.message)
        Result.failure()
    }



    fun toSepia(color: Bitmap): Bitmap {
        var red: Int
        var green: Int
        var blue: Int
        var pixel: Int
        val height = color.height
        val width = color.width
        val depth = 20

        val sepia = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val pixels = IntArray(width * height)
        color.getPixels(pixels, 0, width, 0, 0, width, height)
        for (i in pixels.indices) {
            pixel = pixels[i]

            red = pixel shr 16 and 0xFF
            green = pixel shr 8 and 0xFF
            blue = pixel and 0xFF

            blue = (red + green + blue) / 3
            green = blue
            red = green

            red += depth * 2
            green += depth

            if (red > 255)
                red = 255
            if (green > 255)
                green = 255
            pixels[i] = 0xFF shl 24 or (red shl 16) or (green shl 8) or blue
        }
        sepia.setPixels(pixels, 0, width, 0, 0, width, height)
        return sepia
    }
}