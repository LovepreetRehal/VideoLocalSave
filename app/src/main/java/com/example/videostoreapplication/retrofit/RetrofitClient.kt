package com.example.casinoo.retrofit

import com.google.gson.GsonBuilder
import com.app.casino.retrofit.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitClient {

    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var REST_CLIENT: ApiInterface


        fun getRetrofitInstance(): Retrofit {
            return retrofit
        }


        // live
//        val baseUrl = "https://localjackpots.com/api/"
        val baseUrl = "https://api.localjackpots.com/api/"

//        https://localjackpots.com/api

        var gson = GsonBuilder().setLenient().create()

        fun get(): ApiInterface {
            retrofit = Retrofit.Builder().baseUrl(baseUrl).client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson)).build()
            REST_CLIENT = retrofit.create(ApiInterface::class.java)
            return REST_CLIENT
        }

        fun getOkHttpClient(): OkHttpClient {

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(5, TimeUnit.MINUTES)
            builder.readTimeout(5, TimeUnit.MINUTES)
            builder.writeTimeout(5, TimeUnit.MINUTES)
            builder.addNetworkInterceptor(httpLoggingInterceptor)
            builder.protocols(listOf(Protocol.HTTP_1_1))

            builder.addInterceptor { chain ->
                val request = chain.request()
                val header = request.newBuilder().header(
                    "Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5ODM0YzEwZi03YWU1LTQ4Y2ItYWM0NC01MDcyM2VhZWFmZTMiLCJqdGkiOiI5ZjcwMjcyNzEyYzVmNzJjNWRkMWI1MWM1MTFhY2RkNjNkOTNkZDcwZmQ3Mzc1MTYwMWY2YjRlM2FmNWJjYzNkY2I3ZTJhOGIyZTUyNTFkZCIsImlhdCI6MTcyOTQ5MzI2MC41NDY1MTYsIm5iZiI6MTcyOTQ5MzI2MC41NDY1MTcsImV4cCI6MTc2MTAyOTI2MC41NDI0MjEsInN1YiI6IjgxOCIsInNjb3BlcyI6W119.M7Ebb8JCkPviFU1P0479u-0eJl5h3xVnwPt4AZEnzBG_qgHBlVBXoKu9OXluiNotw-trwRlzEIvU9FtlCD3qjGJ9WgqigmVcXFFmxgs7xMMrChosiEtesY3RDdIlJxlTt_SHNluKSz4JVrmJ5xvano6FVBuMezyRu2y0h-yXep1VRo96ntrI5vbq0UO7zpi65kPk1730MRF0EYn5i0KY-JkJTdhOj0u1VWH90sT1qBTroBNmNdRKmf1t-ig_X-uTcsYSbt9LyX5gbcGsR56Nkw_AIpECKHt5EEBQqnG-YuH6J9YrOw-dvo09rvA-u9iSTjI7PL0LJg_9t-SXOXd-OBVLm1CGsHyOPctLRYELko6gZaRhtjOCNhC9A3obQIyj-5BmqgBn6IRQXDe9GFdU3KjhKDcMizfKGMRRX_Ep3DYT1pkeQSem27_5vBKMRQFUBxLjhK1QX-nZON9wn7ZreV3hR4HMxzTNepIgBLYVAMtW5M_Ps6ecThEFDRAdU_bctUqzujM8OAUZbRDf2AXc-PmjnEKrg5uN1hO5XZd-C0JbnL7xJeFfiuPG2WW6xudlWsqZMV7yulJKhP5Xd8aGcHpSbSqMK8aTCac0wYnYhXDwLEWLqwkl3s6w866YGbFFx6AihP4iMYA1H1lpKfkp2tGtgl0-ixHAyLBIjgNPBAQ"
                )
                val build = header.build()
                chain.proceed(build)
            }
            return builder.build()
        }


    }
}