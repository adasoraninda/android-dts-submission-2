package com.adasoraninda.githubuserdts.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adasoraninda.githubuserdts.common.FollowType
import com.adasoraninda.githubuserdts.data.domain.User
import com.adasoraninda.githubuserdts.data.domain.toDomain
import com.adasoraninda.githubuserdts.data.response.UserResponse
import com.adasoraninda.githubuserdts.network.ApiConfig
import com.adasoraninda.githubuserdts.utils.SingleEvent
import com.adasoraninda.githubuserdts.utils.fetch
import retrofit2.Call

private const val TAG = "FollowViewModel"

class FollowViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> get() = _error

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _username = MutableLiveData<SingleEvent<String>>()
    val username: LiveData<SingleEvent<String>> get() = _username

    private var _call: Call<List<UserResponse>>? = null

    override fun onCleared() {
        super.onCleared()
        _call = null
    }

    fun onItemClick(username: String) {
        _username.value = SingleEvent(username)
    }

    fun getUserFollow(type: FollowType, username: String?) {
        _call = if (type == FollowType.FOLLOWERS) {
            ApiConfig.service.getUserFollowers(username)
        } else {
            ApiConfig.service.getUserFollowing(username)
        }

        _call?.fetch(
            pre = { _loading.value = true },
            post = { _loading.value = false },
            onError = { t ->
                Log.e(TAG, "${t.message}")
                _error.value = true
            },
            onSuccess = { response ->
                if (response.isSuccessful && response.code() == 200) {
                    val results = response.body() ?: emptyList()

                    _error.value = results.isEmpty()
                    _users.value = results.map(UserResponse::toDomain)
                }
            }
        )
    }

}