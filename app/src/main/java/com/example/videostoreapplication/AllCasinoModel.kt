package com.app.casino.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllCasinoModel(
    val `data`: ArrayList<AllCasinoData>,
    val message: String? = "",
    val meta: CasinoMeta,
    val status: String? = "",
    val statusCode: Int? = 0
) : Parcelable

@Parcelize
data class AllCasinoData(
    val casino_name: String? = "",
    val comment_enable: String? = "",
    val created_at: String? = "",
    val description: String? = "",
    val id: Int? = 0,
    val live_id: Int? = 0,
    val location: String? = "",
    val machine_name: String? = "",
    val post_fav: Boolean,
    val post_images: List<PostImage>,
    val post_like: Boolean,
    val tag_users: Boolean,
    val thumbnail_video: String? = "",
    val title: String? = "",
    val total_comment: String? = "",
    val total_like: String? = "",
    val total_share: String? = "",
    val total_view: Int? = 0,
    val user: CasinoUser,
    val channel_id: String? = "",
    val live_thumbnail: String? = "",
    val live_viewcount: String? = "",




    ) : Parcelable

@Parcelize
data class CasinoMeta(
    val currentPage: Int? = 0, val lastPage: Int? = 0, val perPage: Int? = 0, val total: Int? = 0
) : Parcelable

@Parcelize
data class PostImage(
    val extension: String? = "",
    val id: Int? = 0,
    val item_name: String? = "",
    val post_id: String? = "",
    val type: String? = ""
) : Parcelable

@Parcelize
data class CasinoUser(
    val id: Int? = 0, val image: String? = "", val name: String? = "",val isfollow:Boolean

) : Parcelable