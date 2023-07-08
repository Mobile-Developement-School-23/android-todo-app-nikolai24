package com.example.todoapp.data.retrofit


import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interface for working with the server
 */

interface TodoApi {

    @GET("list")
    suspend fun getList(): ListResponse

    @PATCH("list")
    suspend fun updateList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body item: ListRequest
    ): ListResponse

    @GET("list/{id}")
    suspend fun getItem(@Path("id") id: String): ItemResponse

    @POST("list")
    suspend fun post(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body item: ItemRequest
    ): ItemResponse

    @PUT("list/{id}")
    suspend fun put(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String,
        @Body item: ItemRequest
    ): ItemResponse

    @DELETE("list/{id}")
    suspend fun delete(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): ItemResponse
}