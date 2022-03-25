package com.githubuserapps.network

import com.githubuserapps.data.model.DetailResponse
import com.githubuserapps.data.model.SearchItems
import com.githubuserapps.data.model.SearchResponse
import com.githubuserapps.util.Constant.Companion.BASE_URL
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private val client = OkHttpClient.Builder()
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(client)
        .build()
        .create(ApiEndPoint::class.java)

    fun getSearchUsers(searchQuery: String): Single<SearchResponse> {
        return retrofit.getSearchUsers(searchQuery)
    }

    fun getFollowers(followerQuery: String): Single<List<SearchItems>> {
        return retrofit.getFollowers(followerQuery)
    }

    fun getFollowing(followingQuery: String): Single<List<SearchItems>> {
        return retrofit.getFollowing(followingQuery)
    }

    fun getDetailUser(detailQuery: String): Single<DetailResponse> {
        return retrofit.getDetailUser(detailQuery)
    }
}