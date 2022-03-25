package com.githubuserapps.data.local.database

import android.app.appsearch.SearchResults
import androidx.room.TypeConverter
import com.githubuserapps.data.model.SearchResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun userDataToString(userData: SearchResponse): String {
        return gson.toJson(userData)
    }

    @TypeConverter
    fun stringToUserData(data: String): SearchResponse {
        val listType = object : TypeToken<SearchResponse>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun userResultToString(userResult: SearchResults): String {
        return gson.toJson(userResult)
    }

    @TypeConverter
    fun stringToUserResult(data: String): SearchResults {
        val listType = object : TypeToken<SearchResults>() {}.type
        return gson.fromJson(data, listType)
    }

}