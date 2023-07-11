package com.example.todoapp.di.modules

import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.presentation.viewmodel.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ViewModelFactoryModule {

    @Provides
    fun provideViewModelFactory(repository: TodoItemsRepository): MainViewModelFactory {
        return MainViewModelFactory(repository)
    }

}