package com.example.todoapp.di.modules

import com.example.todoapp.di.components.AppScope
import com.example.todoapp.data.retrofit.TodoApiImpl
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun networkService(): TodoApiImpl {
        return TodoApiImpl()
    }

}