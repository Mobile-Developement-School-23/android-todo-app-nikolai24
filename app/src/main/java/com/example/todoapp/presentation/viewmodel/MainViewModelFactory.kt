package com.example.todoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.repository.TodoItemsRepository
import javax.inject.Inject
import javax.inject.Scope

@Scope
annotation class ViewModelFactoryScope

@ViewModelFactoryScope
class MainViewModelFactory @Inject constructor(val repository: TodoItemsRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            repository = repository
        ) as T
    }

}