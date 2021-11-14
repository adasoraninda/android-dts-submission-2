package com.adasoraninda.githubuserdts.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adasoraninda.githubuserdts.data.domain.User
import com.adasoraninda.githubuserdts.data.domain.toDomain
import com.adasoraninda.githubuserdts.data.response.SearchResponse
import com.adasoraninda.githubuserdts.data.response.UserResponse
import com.adasoraninda.githubuserdts.service.ApiConfig
import com.adasoraninda.githubuserdts.utils.fetch
import retrofit2.Call

private const val TAG = "ListUserViewModel"

class ListUserViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> get() = _error

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean> get() = _refresh

    private var call: Call<SearchResponse>? = null

    init {
        onQuerySubmit(randomChar())
    }

    fun onRefresh() {
        findUsers(randomChar(), _refresh)
    }

    fun onQuerySubmit(query: String?) {
        findUsers(query, _loading)
    }

    private fun findUsers(username: String?, loadingType: MutableLiveData<Boolean>) {
        ApiConfig.service.findUsers(username).fetch(
            pre = { loadingType.value = true },
            post = { loadingType.value = false },
            onError = { t ->
                Log.e(TAG, "${t.message}")
                _error.value = true
            },
            onSuccess = { response ->
                if (response.isSuccessful && response.code() == 200) {
                    val results = response.body()?.users ?: emptyList()

                    _error.value = results.isEmpty()
                    _users.value = results.map(UserResponse::toDomain)
                }
            },
        ).apply { call = this }
    }

    private fun randomChar(): String {
        return ('a'..'z').random().toString()
    }

    override fun onCleared() {
        super.onCleared()
        call?.cancel()
    }

}