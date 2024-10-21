package com.app.casino.retrofit


import com.app.casino.data.AllCasinoModel
import retrofit2.Response

class UserRepository {

    private val MULTIPART_FORM_DATA = "multipart/form-data"


    suspend fun allHomePost(
        id: String, page: Int, sort_by: String
    ): Response<AllCasinoModel> {
        return UserNetwork.retrofit.allHomePost(id, page, sort_by)
    }


}