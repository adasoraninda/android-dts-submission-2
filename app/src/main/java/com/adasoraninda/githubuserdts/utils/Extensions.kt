package com.adasoraninda.githubuserdts.utils

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T : ViewModel> ViewModelStoreOwner.obtainViewModel(
    jClass: Class<T>
): T {
    return ViewModelProvider(this)[jClass]
}

fun <T : ViewModel> ViewModelStoreOwner.obtainViewModelWithFactory(
    jClass: Class<T>,
    factory: ViewModelProvider.Factory
): T {
    return ViewModelProvider(this, factory)[jClass]
}

fun Context.showToastMessage(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun <T> Call<T>.fetch(
    pre: () -> Unit,
    onSuccess: (response: Response<T>) -> Unit,
    onError: (t: Throwable) -> Unit,
    post: () -> Unit,
): Call<T> {
    pre()

    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            onSuccess(response)
            post()
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onError(t)
            post()
        }
    })

    return this
}