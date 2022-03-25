package com.githubuserapps.network

import com.githubuserapps.data.model.DetailResponse
import com.githubuserapps.data.model.SearchItems
import com.githubuserapps.data.model.SearchResponse
import com.githubuserapps.util.Constant.Companion.TOKEN
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndPoint {

    @GET("search/users")
    @Headers("Authorization: token $TOKEN") // $TOKEN input your token from Github Personal Access Tokens
    fun getSearchUsers(
        @Query("q") searchQuery: String?
    ): Single<SearchResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $TOKEN")
    fun getFollowers(
        @Path("username") followersQuery: String?
    ): Single<List<SearchItems>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $TOKEN")
    fun getFollowing(
        @Path("username") followingQuery: String?
    ): Single<List<SearchItems>>

    @GET("users/{username}")
    @Headers("Authorization: token $TOKEN")
    fun getDetailUser(
        @Path("username") detailQuery: String?
    ): Single<DetailResponse>

}