package com.app.casino.retrofit

import android.util.Base64
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream

object RetrofitUtils {
    private const val MULTIPART_FORM_DATA = "multipart/form-data"

    fun stringToRequestBody(string: String): RequestBody {
        return string.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
    }
    fun createPartFromFile(partName: String, file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
    // Function to convert a file to Base64 string
    fun fileToBase64(file: File): String {
        val inputStream = FileInputStream(file)
        val bytes = ByteArray(file.length().toInt())
        inputStream.read(bytes)
        inputStream.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}
