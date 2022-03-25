package com.githubuserapps.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.githubuserapps.data.local.database.UserDao
import com.githubuserapps.data.local.database.UserDatabase
import com.githubuserapps.data.local.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    // ROOM
    private var userDao: UserDao
    private var userDb: UserDatabase = UserDatabase.buildDatabase(application)

    init {
        userDao = userDb.userDao()
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> {
        return userDao.getFavoriteUser()
    }

    fun deleteAllFavoriteUser() {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.deleteAllFavoriteUser()
        }
    }

}