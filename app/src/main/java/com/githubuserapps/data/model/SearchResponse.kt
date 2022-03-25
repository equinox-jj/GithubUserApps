package com.githubuserapps.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchResponse(
    @field:SerializedName("items")
    val searchItems: List<SearchItems>
)

@Parcelize
data class SearchItems(

    @field:SerializedName("login")
    val login: String? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("html_url")
    val htmlUrl: String? = null

) : Parcelable