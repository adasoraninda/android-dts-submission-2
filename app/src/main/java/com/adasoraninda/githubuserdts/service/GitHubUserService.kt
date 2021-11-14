package com.adasoraninda.githubuserdts.service

import com.adasoraninda.githubuserdts.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubUserService {

    companion object {
        const val BASE_URL = "http://api.github.com"
    }

    @GET("search/users")
    fun findUsers(
        @Query("q") query: String?
    ): Call<SearchResponse>

}