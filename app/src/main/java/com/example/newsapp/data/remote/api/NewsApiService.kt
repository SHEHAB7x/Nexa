package com.example.newsapp.data.remote.api

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/everything")
    suspend fun getAppleNews(
        @Query("q")       q: String = "apple",
        @Query("from")    from: String = "2026-04-04",
        @Query("to")      to: String = "2026-04-04",
        @Query("sortBy")  sortBy: String = "popularity",
        @Query("language") language: String = "en",
        @Query("page")     page: Int      = 1,
        @Query("pageSize") pageSize: Int  = 20,
        @Query("apiKey")  apiKey: String = API_KEY
    ): NewsResponseDto

    @GET("v2/everything")
    suspend fun getTeslaNews(
        @Query("q")       q: String = "tesla",
        @Query("from")    from: String = "2026-03-08",
        @Query("sortBy")  sortBy: String = "publishedAt",
        @Query("language") language: String = "en",
        @Query("page")     page: Int      = 1,
        @Query("pageSize") pageSize: Int  = 20,
        @Query("apiKey")  apiKey: String = API_KEY
    ): NewsResponseDto

    @GET("v2/top-headlines")
    suspend fun getBusinessNews(
        @Query("country")  country: String = "us",
        @Query("category") category: String = "business",
        @Query("page")     page: Int      = 1,
        @Query("pageSize") pageSize: Int  = 20,
        @Query("apiKey")   apiKey: String = API_KEY
    ): NewsResponseDto

    @GET("v2/everything")
    suspend fun getWsjNews(
        @Query("domains") domains: String = "wsj.com",
        @Query("language") language: String = "en",
        @Query("page")     page: Int      = 1,
        @Query("pageSize") pageSize: Int  = 20,
        @Query("apiKey")  apiKey: String = API_KEY
    ): NewsResponseDto

    @GET("v2/top-headlines")
    suspend fun getTechCrunchNews(
        @Query("sources") sources: String = "techcrunch",
        @Query("page")     page: Int      = 1,
        @Query("pageSize") pageSize: Int  = 20,
        @Query("apiKey")  apiKey: String = API_KEY
    ): NewsResponseDto

    companion object {
        const val BASE_URL = "https://newsapi.org/"
        const val API_KEY  = BuildConfig.NEWS_API_KEY
    }
}
