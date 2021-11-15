package com.adasoraninda.githubuserdts.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adasoraninda.githubuserdts.data.domain.User
import com.adasoraninda.githubuserdts.data.domain.toDomain
import com.adasoraninda.githubuserdts.data.response.UserResponse
import com.adasoraninda.githubuserdts.network.ApiConfig
import com.adasoraninda.githubuserdts.utils.SingleEvent
import com.adasoraninda.githubuserdts.utils.fetch
import retrofit2.Call

private const val TAG = "DetailUserViewModel"

class DetailUserViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> get() = _error

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _share = MutableLiveData<SingleEvent<User>>()
    val share: LiveData<SingleEvent<User>> get() = _share

    private val _back = MutableLiveData<SingleEvent<Boolean>>()
    val back: LiveData<SingleEvent<Boolean>> get() = _back

    private var _call: Call<UserResponse>? = null

    override fun onCleared() {
        super.onCleared()
        _call = null
    }

    fun shareUser() {
        _user.value?.let {
            _share.value = SingleEvent(it)
        }
    }

    fun onBackClicked() {
        _back.value = SingleEvent(true)
    }

    fun getDetailUser(username: String?) {
        Log.d(TAG,"$username")
        ApiConfig.service.getDetailUser(username).fetch(
            pre = { _loading.value = true },
            post = { _loading.value = false },
            onError = { t ->
                Log.e(TAG, "${t.message}")
                _error.value = true
            },
            onSuccess = { response ->
                if (response.isSuccessful && response.code() == 200) {
                    val results = response.body()

                    _error.value = results == null
                    _user.value = results?.toDomain()
                }else{
                    _error.value = true
                }
            }
        ).apply { _call = this }
    }
}