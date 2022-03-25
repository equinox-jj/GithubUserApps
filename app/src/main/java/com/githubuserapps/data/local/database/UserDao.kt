package com.githubuserapps.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.githubuserapps.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_favorite_table ORDER BY id ASC")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteUser(userFavoriteEntity: UserEntity)

    @Delete
    suspend fun deleteFavoriteUser(userFavoriteEntity: UserEntity)

    @Query("DELETE FROM user_favorite_table")
    suspend fun deleteAllFavoriteUser()

}