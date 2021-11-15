package com.adasoraninda.githubuserdts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adasoraninda.githubuserdts.utils.SingleEvent
import kotlinx.coroutines.*

private const val DELAY_DURATION = 2_000L

class SplashViewModel : ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private val _navigation = MutableLiveData<SingleEvent<Boolean>>()
    val navigation: LiveData<SingleEvent<Boolean>> get() = _navigation

    init {
        doNavigation()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    private fun doNavigation() {
        coroutineScope.launch {
            delay(DELAY_DURATION)
            _navigation.value = SingleEvent(true)
        }
    }

}