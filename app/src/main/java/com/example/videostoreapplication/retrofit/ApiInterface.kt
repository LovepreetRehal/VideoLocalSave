package com.app.casino.retrofit

import com.app.casino.data.AllCasinoModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {


    @GET("posts_by_casino/{id}")
    suspend fun allHomePost(
        @Path("id") id: String, @Query("page") page: Int, @Query("sort_by") sort_by: String
    ): Response<AllCasinoModel>



}

