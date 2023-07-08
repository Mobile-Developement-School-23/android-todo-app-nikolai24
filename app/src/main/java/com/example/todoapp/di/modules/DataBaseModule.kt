package com.example.todoapp.di.modules

import android.content.Context
import com.example.todoapp.data.database.TodoDao
import com.example.todoapp.data.database.TodoDatabase
import com.example.todoapp.di.components.AppScope
import com.example.todoapp.data.repository.Repository
import com.example.todoapp.data.retrofit.TodoApiImpl
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {

    @Provides
    @AppScope
    fun provideDataBase(context: Context): TodoDatabase {
        return TodoDatabase.getDatabase(context)
    }

    @Provides
    @AppScope
    fun provideTodoDao(todoDataBase: TodoDatabase): TodoDao {
        return todoDataBase.todoDao()
    }

}