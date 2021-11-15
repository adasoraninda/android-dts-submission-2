package com.adasoraninda.githubuserdts.utils

class SingleEvent<out T>(private val data: T) {

    private var isHandled = false

    fun getContent(): T? {
        return if (isHandled) {
            null
        } else {
            isHandled = true
            data
        }
    }

}