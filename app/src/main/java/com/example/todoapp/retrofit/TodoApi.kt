package com.example.todoapp.retrofit

import com.example.todoapp.database.TodoItem
import com.example.todoapp.utils.AppConstants.BASE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.io.IOException

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

object TodoApiImpl {

    var revision: Int = 0

    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(AuthInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(TodoApi::class.java)

    suspend fun getList(): List<TodoItem> {
        var getResponse = withContext(Dispatchers.IO) {
            service.getList()
        }
        revision = getResponse.revision
        return getResponse.list.map { x -> x.toTodoItem() }
    }

    suspend fun updateList(list: List<TodoItem>) {
        var listRequest = list.map { x -> x.toTodoItemData() }
        withContext(Dispatchers.IO) {
            service.updateList(1, ListRequest(listRequest))
        }
    }

    suspend fun post(item: TodoItem) {
        var getResponse = withContext(Dispatchers.IO) {
            service.getList()
        }
        revision = getResponse.revision
        try {
            withContext(Dispatchers.IO){
                service.post(revision, ItemRequest(item.toTodoItemData()))
            }
        } catch (e: HttpException) {
            println(e.message)
        }
    }

    suspend fun put(item: TodoItem) {
        var getResponse = withContext(Dispatchers.IO) {
            service.getList()
        }
        revision = getResponse.revision
        try {
            withContext(Dispatchers.IO) {
                service.put(revision, item.id, ItemRequest(item.toTodoItemData()))
            }
        } catch (e: HttpException) {
            println(e.message)
        }
    }

    suspend fun delete(item: TodoItem) {
        var getResponse = withContext(Dispatchers.IO) {
            service.getList()
        }
        revision = getResponse.revision
        try {
            withContext(Dispatchers.IO) {
                service.delete(revision, item.id)
            }
        } catch (e: HttpException) {
            println(e.message)
        }
    }
}