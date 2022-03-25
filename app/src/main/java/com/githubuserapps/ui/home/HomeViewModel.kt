package com.githubuserapps.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.githubuserapps.data.model.SearchItems
import com.githubuserapps.data.model.SearchResponse
import com.githubuserapps.network.ApiConfig
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    // RETROFIT
    private val apiConfig = ApiConfig
    private val disposable = CompositeDisposable()

    val searchUser = MutableLiveData<List<SearchItems>>()
    val isLoading = MutableLiveData<Boolean>()
    val isError = MutableLiveData<Boolean>()

    fun searchList(searchQuery: String) {
        isLoading.value = true
        disposable.add(
            apiConfig.getSearchUsers(searchQuery)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SearchResponse>() {
                    override fun onSuccess(response: SearchResponse) {
                        searchUser.value = response.searchItems
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

