package com.githubuserapps.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.githubuserapps.data.local.database.UserDao
import com.githubuserapps.data.local.database.UserDatabase
import com.githubuserapps.data.local.entity.UserEntity
import com.githubuserapps.data.model.DetailResponse
import com.githubuserapps.network.ApiConfig
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    // ROOM
    private var userDao: UserDao
    private var userDb: UserDatabase = UserDatabase.buildDatabase(application)

    init {
        userDao = userDb.userDao()
    }

    // RETROFIT
    private val apiConfig = ApiConfig
    private val disposable = CompositeDisposable()

    val detailUser = MutableLiveData<DetailResponse>()
    val isLoading = MutableLiveData<Boolean>()
    val isError = MutableLiveData<Boolean>()

    fun detailUser(username: String) {
        isLoading.value = true
        disposable.add(
            apiConfig.getDetailUser(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<DetailResponse>() {
                    override fun onSuccess(response: DetailResponse) {
                        detailUser.value = response
                        isError.value = false
                        isLoading.value = false
                    }

                    override fun onError(e: Throwable) {
                        isError.value = true
                        isLoading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    // FAVORITE USER
    fun insertUserToFavorite(id: Int, username: String, avatar_url: String, html_url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserEntity(
                id,
                username,
                avatar_url,
                html_url
            )
            userDao.insertFavoriteUser(user)
        }
    }

    fun deleteFavoriteUser(id: Int, userName: String, avatar_url: String, html_url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserEntity(
                id,
                userName,
                avatar_url,
                html_url
            )
            userDao.deleteFavoriteUser(user)
        }
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> {
        return userDao.getFavoriteUser()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}