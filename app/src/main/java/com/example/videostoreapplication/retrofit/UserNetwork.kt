package com.app.casino.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object UserNetwork {
    var BASEURL: String = "https://api.localjackpots.com/api/"

    val retrofit: ApiInterface by lazy {


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor)
            .addNetworkInterceptor(header!!)

            .connectTimeout(50, TimeUnit.MINUTES)
            .writeTimeout(50, TimeUnit.MINUTES)
            .readTimeout(50, TimeUnit.MINUTES)

            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(ApiInterface::class.java)



    }


    private var header: Interceptor? = Interceptor { chain ->

        val newRequest: Request = chain.request().newBuilder()
            .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5ODM0YzEwZi03YWU1LTQ4Y2ItYWM0NC01MDcyM2VhZWFmZTMiLCJqdGkiOiI5ZjcwMjcyNzEyYzVmNzJjNWRkMWI1MWM1MTFhY2RkNjNkOTNkZDcwZmQ3Mzc1MTYwMWY2YjRlM2FmNWJjYzNkY2I3ZTJhOGIyZTUyNTFkZCIsImlhdCI6MTcyOTQ5MzI2MC41NDY1MTYsIm5iZiI6MTcyOTQ5MzI2MC41NDY1MTcsImV4cCI6MTc2MTAyOTI2MC41NDI0MjEsInN1YiI6IjgxOCIsInNjb3BlcyI6W119.M7Ebb8JCkPviFU1P0479u-0eJl5h3xVnwPt4AZEnzBG_qgHBlVBXoKu9OXluiNotw-trwRlzEIvU9FtlCD3qjGJ9WgqigmVcXFFmxgs7xMMrChosiEtesY3RDdIlJxlTt_SHNluKSz4JVrmJ5xvano6FVBuMezyRu2y0h-yXep1VRo96ntrI5vbq0UO7zpi65kPk1730MRF0EYn5i0KY-JkJTdhOj0u1VWH90sT1qBTroBNmNdRKmf1t-ig_X-uTcsYSbt9LyX5gbcGsR56Nkw_AIpECKHt5EEBQqnG-YuH6J9YrOw-dvo09rvA-u9iSTjI7PL0LJg_9t-SXOXd-OBVLm1CGsHyOPctLRYELko6gZaRhtjOCNhC9A3obQIyj-5BmqgBn6IRQXDe9GFdU3KjhKDcMizfKGMRRX_Ep3DYT1pkeQSem27_5vBKMRQFUBxLjhK1QX-nZON9wn7ZreV3hR4HMxzTNepIgBLYVAMtW5M_Ps6ecThEFDRAdU_bctUqzujM8OAUZbRDf2AXc-PmjnEKrg5uN1hO5XZd-C0JbnL7xJeFfiuPG2WW6xudlWsqZMV7yulJKhP5Xd8aGcHpSbSqMK8aTCac0wYnYhXDwLEWLqwkl3s6w866YGbFFx6AihP4iMYA1H1lpKfkp2tGtgl0-ixHAyLBIjgNPBAQ")
            .build()

        Log.e("accessToken", "accessToken12456  -: eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5ODM0YzEwZi03YWU1LTQ4Y2ItYWM0NC01MDcyM2VhZWFmZTMiLCJqdGkiOiI5ZjcwMjcyNzEyYzVmNzJjNWRkMWI1MWM1MTFhY2RkNjNkOTNkZDcwZmQ3Mzc1MTYwMWY2YjRlM2FmNWJjYzNkY2I3ZTJhOGIyZTUyNTFkZCIsImlhdCI6MTcyOTQ5MzI2MC41NDY1MTYsIm5iZiI6MTcyOTQ5MzI2MC41NDY1MTcsImV4cCI6MTc2MTAyOTI2MC41NDI0MjEsInN1YiI6IjgxOCIsInNjb3BlcyI6W119.M7Ebb8JCkPviFU1P0479u-0eJl5h3xVnwPt4AZEnzBG_qgHBlVBXoKu9OXluiNotw-trwRlzEIvU9FtlCD3qjGJ9WgqigmVcXFFmxgs7xMMrChosiEtesY3RDdIlJxlTt_SHNluKSz4JVrmJ5xvano6FVBuMezyRu2y0h-yXep1VRo96ntrI5vbq0UO7zpi65kPk1730MRF0EYn5i0KY-JkJTdhOj0u1VWH90sT1qBTroBNmNdRKmf1t-ig_X-uTcsYSbt9LyX5gbcGsR56Nkw_AIpECKHt5EEBQqnG-YuH6J9YrOw-dvo09rvA-u9iSTjI7PL0LJg_9t-SXOXd-OBVLm1CGsHyOPctLRYELko6gZaRhtjOCNhC9A3obQIyj-5BmqgBn6IRQXDe9GFdU3KjhKDcMizfKGMRRX_Ep3DYT1pkeQSem27_5vBKMRQFUBxLjhK1QX-nZON9wn7ZreV3hR4HMxzTNepIgBLYVAMtW5M_Ps6ecThEFDRAdU_bctUqzujM8OAUZbRDf2AXc-PmjnEKrg5uN1hO5XZd-C0JbnL7xJeFfiuPG2WW6xudlWsqZMV7yulJKhP5Xd8aGcHpSbSqMK8aTCac0wYnYhXDwLEWLqwkl3s6w866YGbFFx6AihP4iMYA1H1lpKfkp2tGtgl0-ixHAyLBIjgNPBAQ" )

        chain.proceed(newRequest)

    }
}