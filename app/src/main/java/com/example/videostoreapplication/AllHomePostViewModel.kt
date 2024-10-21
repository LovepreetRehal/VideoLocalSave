package com.example.videostoreapplication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.casino.data.AllCasinoModel
import com.app.casino.retrofit.BaseResponse
import com.app.casino.retrofit.UserRepository

import kotlinx.coroutines.launch

class AllHomePostViewModel : ViewModel() {
    var getPostResponse: MutableLiveData<BaseResponse<AllCasinoModel>?> = MutableLiveData()
    val userRepo = UserRepository()

    fun getHomePost(id:String,page:Int,sortBy:String) {
        getPostResponse.value = null
        getPostResponse.value = BaseResponse.Loading()


        viewModelScope.launch {
            try {
                val response = userRepo.allHomePost(id,page,sortBy)
                Log.e("getHomePost", "getHomePost: ${response.body()}")

                if (response.code() == 200) {
                    getPostResponse.value = null

                    getPostResponse.value = BaseResponse.Success(response.body())
                } else {
                    getPostResponse.value = null

                    getPostResponse.value = BaseResponse.Error(response.message())
                }

            } catch (ae: Exception) {
                ae.printStackTrace()
                Log.e("getHomePost", "getHomePost  Error -> : $ae")
            }

        }


    }

}