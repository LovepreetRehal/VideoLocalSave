package com.app.casino.retrofit

data class PojoCreateListing(
    val `data`: DataXX,
    val message: String,
    val status: String,
    val statusCode: Int
)
data class DataXX(
    val budget: Any,
    val category_id: String,
    val city: Any,
    val condition: Any,
    val country: Any,
    val created_at: String,
    val description: String,
    val estimated_time: Any,
    val id: Int,
    val lat: Any,
    val lng: Any,
    val location: String,
    val price: String,
    val state: Any,
    val status: Int,
    val sub_category_id: String,
    val title: String,
    val type: String,
    val updated_at: String,
    val user_id: Int
)