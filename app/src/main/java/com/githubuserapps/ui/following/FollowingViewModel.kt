package com.githubuserapps.ui.following

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.githubuserapps.data.model.SearchItems
import com.githubuserapps.network.ApiConfig
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class FollowingViewModel : ViewModel() {

    private val apiConfig = ApiConfig
    private val disposable = CompositeDisposable()

    val followingUser = MutableLiveData<List<SearchItems>>()
    val isLoading = MutableLiveData<Boolean>()
    val isError = MutableLiveData<Boolean>()

    fun followingUserList(followingQuery: String) {
        isLoading.value = true
        disposable.add(
            apiConfig.getFollowing(followingQuery)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<SearchItems>>() {
                    override fun onSuccess(response: List<SearchItems>) {
                        followingUser.value = response
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

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}