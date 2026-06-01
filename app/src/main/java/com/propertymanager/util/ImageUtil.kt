package com.propertymanager.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageUtil {

    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val photoDir = File(context.filesDir, "photos")
            photoDir.mkdirs()
            
            val timeStamp = System.currentTimeMillis()
            val fileName = "IMG_$timeStamp.jpg"
            val photoFile = File(photoDir, fileName)
            
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(photoFile).use { output ->
                    input.copyTo(output)
                }
            }
            
            photoFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        return try {
            val filePathColumn = arrayOf(android.provider.MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    it.getString(it.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA))
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun compressImage(context: Context, uri: Uri, maxWidth: Int = 1920, maxHeight: Int = 1920): Uri? {
        return try {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri) ?: return null
            val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            
            val ratio = minOf(
                maxWidth.toFloat() / bitmap.width,
                maxHeight.toFloat() / bitmap.height
            )
            
            val newWidth = (bitmap.width * ratio).toInt()
            val newHeight = (bitmap.height * ratio).toInt()
            
            val compressedBitmap = android.graphics.Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
            
            val photoDir = File(context.cacheDir, "compressed")
            photoDir.mkdirs()
            
            val fileName = "compressed_${System.currentTimeMillis()}.jpg"
            val file = File(photoDir, fileName)
            
            FileOutputStream(file).use { out ->
                compressedBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, out)
            }
            
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
