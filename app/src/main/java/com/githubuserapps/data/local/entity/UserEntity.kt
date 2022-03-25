package com.githubuserapps.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_favorite_table")
class UserEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    var username: String,
    var avatar_url: String,
    var html_url: String
)