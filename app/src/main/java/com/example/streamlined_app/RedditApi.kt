package com.example.streamlined_app

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// Retrofit interface for Reddit search
interface RedditApiService {
    @GET("search.json")
    suspend fun searchReddit(
        @Query("q") query: String,
        @Query("sort") sort: String = "top",
        @Query("limit") limit: Int = 5
    ): RedditSearchResponse
}

// Data models matching Reddit search JSON
data class RedditSearchResponse(val data: RedditData)
data class RedditData(val children: List<RedditChild>)
data class RedditChild(val data: RedditPost)
data class RedditPost(
    val title: String,
    val author: String,
    val ups: Int,
    val num_comments: Int
)

// Provide a Retrofit instance ready to use
object RedditApi {
    val service: RedditApiService = Retrofit.Builder()
        .baseUrl("https://www.reddit.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(RedditApiService::class.java)
}