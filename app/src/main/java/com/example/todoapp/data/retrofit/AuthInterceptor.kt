package com.example.todoapp.data.retrofit

import com.example.todoapp.utils.AppConstants.TOKEN
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Creating an Authorization Header.
 */

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder().header(
            "Authorization", "Bearer $TOKEN"
        )
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}