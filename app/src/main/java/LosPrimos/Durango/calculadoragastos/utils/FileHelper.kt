package LosPrimos.Durango.calculadoragastos.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
    return try {
        val fileName = "profile_picture_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}