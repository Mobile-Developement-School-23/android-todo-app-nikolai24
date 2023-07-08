package com.example.todoapp.data.retrofit

import com.example.todoapp.data.database.TodoItem
import com.example.todoapp.utils.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Implementation of the interface for working with the server
 */

class TodoApiImpl {
    private var revision: Int = 0

    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(AuthInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(AppConstants.BASE_URL)
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
            withContext(Dispatchers.IO) {
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