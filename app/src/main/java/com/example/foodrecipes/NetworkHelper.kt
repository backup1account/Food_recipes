package com.example.foodrecipes

import okhttp3.*

object NetworkHelper {
    private const val url = "https://tasty.p.rapidapi.com/recipes/list?from=0&size=70"
    val client = OkHttpClient()

    fun getRequest(): Request {
        return Request.Builder()
            .url(url)
            .get()
            .addHeader("X-RapidAPI-Key", BuildConfig.API_KEY)
            .addHeader("X-RapidAPI-Host", "tasty.p.rapidapi.com")
            .build()
    }
}