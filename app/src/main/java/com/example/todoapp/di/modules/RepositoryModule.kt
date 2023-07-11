package com.example.todoapp.di.modules

import android.content.Context
import com.example.todoapp.data.database.TodoDao
import com.example.todoapp.di.components.AppScope
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.data.retrofit.TodoApiImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    @AppScope
    fun provideRepository(networkService: TodoApiImpl, todoDao: TodoDao, context: Context): TodoItemsRepository {
        return TodoItemsRepository(networkService, todoDao, context)
    }

}