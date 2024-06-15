package com.cosine.kidomo.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Throws(IOException::class)
fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
    // Create a file in the cache directory to hold the bitmap
    val file = File(context.cacheDir, "resized_image.jpg")
    file.createNewFile()

    // Write the bitmap to the file
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    // Get the Uri for the file
    return Uri.fromFile(file)
}

fun resizeImage(bitmap: Bitmap, reqWidth: Int, reqHeight: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    if (width <= reqWidth && height <= reqHeight) return bitmap // If image is smaller than required size, return the original bitmap

    val scaleWidth = reqWidth.toFloat() / width
    val scaleHeight = reqHeight.toFloat() / height

    // Create a matrix for the manipulation
    val matrix = Matrix()

    // Resize the bitmap
    matrix.postScale(scaleWidth, scaleHeight)

    // Recreate the new Bitmap
    return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {

        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}